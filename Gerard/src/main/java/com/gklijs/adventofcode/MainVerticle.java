package com.gklijs.adventofcode;

import io.vertx.core.Future;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.common.template.TemplateEngine;
import io.vertx.reactivex.ext.web.templ.freemarker.FreeMarkerTemplateEngine;

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> startFuture) {
        final TemplateEngine engine = FreeMarkerTemplateEngine.create(vertx);
        Router router = Routes.build(vertx, engine);
        HttpServer server = vertx.createHttpServer().requestHandler(router);
        server.listen(8080);
    }
}
