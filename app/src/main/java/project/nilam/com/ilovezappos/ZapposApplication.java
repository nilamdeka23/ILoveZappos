package project.nilam.com.ilovezappos;

import android.app.Application;
import android.content.Context;

import project.nilam.com.ilovezappos.model.ZapposService;
import rx.Scheduler;
import rx.schedulers.Schedulers;


public class ZapposApplication extends Application {

    private ZapposService zapposService;
    private Scheduler defaultSubscribeScheduler;

    public static ZapposApplication get(Context context) {
        return (ZapposApplication) context.getApplicationContext();
    }

    public ZapposService getZapposService() {
        if (zapposService == null) {
            zapposService = ZapposService.Factory.create();
        }
        return zapposService;
    }

    public Scheduler defaultSubscribeScheduler() {
        if (defaultSubscribeScheduler == null) {
            defaultSubscribeScheduler = Schedulers.io();
        }
        return defaultSubscribeScheduler;
    }
}
