package com.gklijs.adventofcode;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.http.HttpServerRequest;
import io.vertx.reactivex.core.http.HttpServerResponse;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.reactivex.ext.web.common.template.TemplateEngine;
import static com.gklijs.adventofcode.Answers.ANS;
import static io.reactivex.Observable.fromIterable;

class Routes {

    private static final String DAY = "day";
    private static final String PART = "part";
    private static final String INPUT = "input";
    private static final String ERROR = "error";
    private static final String RESULT = "result";

    private Routes() {
        //prevent instantiation
    }

    static Router build(Vertx vertx, TemplateEngine engine) {
        Router mainRouter = Router.router(vertx);
        mainRouter.get("/").handler(questionRequestHandler(engine));
        mainRouter.post("/").handler(postRequestHandler(engine));
        return mainRouter;
    }

    private static void setHeaders(HttpServerResponse response) {
        response.putHeader("content-type", "text/html");
    }

    private static Handler<RoutingContext> questionRequestHandler(TemplateEngine engine) {
        return context -> {
            setHeaders(context.response());
            engine.rxRender(new JsonObject(), "question.ftl")
                .doOnSuccess(result -> context.response().end(result))
                .doOnError(t -> context.response().end(t.toString()))
                .subscribe();
        };
    }

    private static Handler<RoutingContext> postRequestHandler(TemplateEngine engine) {
        return context -> {
            setHeaders(context.response());
            context.request().setExpectMultipart(true);
            context.request().endHandler(setResultHandler(engine, context.request()));
        };
    }

    private static Handler<Void> setResultHandler(TemplateEngine engine, HttpServerRequest req) {
        return v -> {
            List<String> input = Arrays.stream(req.getFormAttribute(INPUT)
                .split("\n"))
                .map(String::trim)
                .collect(Collectors.toList());
            String day = req.getFormAttribute(DAY);
            String part = req.getFormAttribute(PART);
            getPostContext(input, day, part)
                .flatMap(json -> engine.rxRender(json, json.containsKey(RESULT) ? "success.ftl" : "error.ftl"))
                .doOnSuccess(result -> req.response().end(result))
                .doOnError(t -> req.response().end(t.toString()))
                .subscribe();
        };
    }

    private static JsonObject nullCheck(JsonObject jsonObject, List<String> input) {
        if (jsonObject.getString(DAY) == null) {
            jsonObject.put(ERROR, "day is null");
            return jsonObject;
        }
        if (jsonObject.getString(PART) == null) {
            jsonObject.put(ERROR, "part is null");
            return jsonObject;
        }
        if (input == null || input.isEmpty()) {
            jsonObject.put(ERROR, "input is null or empty");
            return jsonObject;
        }
        return jsonObject;
    }

    private static Single<JsonObject> getResult(JsonObject jsonObject, List<String> input) {
        if (jsonObject.getString(ERROR) != null) {
            return Single.just(jsonObject);
        }
        Function<Flowable<String>, Single<String>> answerFunction = null;
        try {
            int dayInt = Integer.parseInt(jsonObject.getString(DAY));
            answerFunction = "1".equals(jsonObject.getString(PART)) ? ANS.get(dayInt).getFirst() : ANS.get(dayInt).getSecond();
        } catch (NullPointerException | NumberFormatException e) {
            jsonObject.put(ERROR, e.toString());
        }
        if (answerFunction == null) {
            return Single.just(jsonObject);
        }
        return answerFunction.apply(fromIterable(input).toFlowable(BackpressureStrategy.BUFFER))
            .map(answer -> jsonObject.put(RESULT, answer))
            .onErrorReturn(t -> jsonObject.put(ERROR, t.toString()));
    }

    private static Single<JsonObject> getPostContext(List<String> input, String day, String part) {
        JsonObject result = new JsonObject();
        result.put(DAY, day);
        result.put(PART, part);
        return Single.just(result)
            .map(j -> nullCheck(j, input))
            .flatMap(j -> getResult(j, input));
    }
}
