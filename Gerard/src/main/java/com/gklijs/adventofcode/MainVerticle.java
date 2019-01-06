package com.gklijs.adventofcode;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.http.HttpServerRequest;
import io.vertx.reactivex.ext.web.common.template.TemplateEngine;
import io.vertx.reactivex.ext.web.templ.freemarker.FreeMarkerTemplateEngine;
import static com.gklijs.adventofcode.Answers.ANS;
import static io.reactivex.Observable.fromIterable;

public class MainVerticle extends AbstractVerticle {

    private static final Logger LOGGER = Logger.getLogger(MainVerticle.class.getName());
    private static final String DAY = "day";
    private static final String PART = "part";
    private static final String INPUT = "input";
    private static final String ERROR = "error";
    private static final String RESULT = "result";

    @Override
    public void start(Future<Void> startFuture) {
        final TemplateEngine engine = FreeMarkerTemplateEngine.create(vertx);
        vertx.createHttpServer()
            .requestHandler(getRequestHandler(engine)).listen(8080, http -> {
            if (http.succeeded()) {
                startFuture.complete();
                LOGGER.info("HTTP server started on http://localhost:8080");
            } else {
                startFuture.fail(http.cause());
            }
        });
    }

    private Handler<Void> postHandler(TemplateEngine engine, HttpServerRequest req) {
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

    private Single<JsonObject> getPostContext(List<String> input, String day, String part) {
        JsonObject result = new JsonObject();
        result.put(DAY, day);
        result.put(PART, part);
        if (input == null || input.isEmpty() || day == null || part == null) {
            result.put(ERROR, "one of the parameters is null, or input is empty");
            return Single.just(result);
        }
        Function<Flowable<String>, Single<String>> answerFunction = null;
        try {
            int dayInt = Integer.parseInt(day);
            answerFunction = "1".equals(part) ? ANS.get(dayInt).getFirst() : ANS.get(dayInt).getSecond();
        } catch (NullPointerException | NumberFormatException e) {
            result.put(ERROR, e.toString());
            return Single.just(result);
        }
        return answerFunction.apply(fromIterable(input).toFlowable(BackpressureStrategy.BUFFER))
            .map(answer -> result.put(RESULT, answer))
            .onErrorReturn(t -> result.put(ERROR, t.toString()));
    }

    private Handler<HttpServerRequest> getRequestHandler(TemplateEngine engine) {
        return req -> {
            req.response()
                .putHeader("content-type", "text/html");
            if (req.method().equals(HttpMethod.POST)) {
                req.setExpectMultipart(true);
                req.endHandler(postHandler(engine, req));
            } else {
                engine.rxRender(new JsonObject(), "question.ftl")
                    .doOnSuccess(result -> req.response().end(result))
                    .doOnError(t -> req.response().end(t.toString()))
                    .subscribe();
            }
        };
    }
}
