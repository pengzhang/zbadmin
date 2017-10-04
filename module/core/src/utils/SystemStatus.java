package utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jamonapi.MonitorFactory;
import com.jamonapi.utils.Misc;

import play.Invoker;
import play.Play;

public class SystemStatus {
	
	public static JsonObject getJsonStatus() {
        JsonObject status = new JsonObject();

        {
            JsonObject java = new JsonObject();
            java.addProperty("version", System.getProperty("java.version"));
            status.add("java", java);
        }

        {
            JsonObject memory = new JsonObject();
            memory.addProperty("max", Runtime.getRuntime().maxMemory());
            memory.addProperty("free", Runtime.getRuntime().freeMemory());
            memory.addProperty("total", Runtime.getRuntime().totalMemory());
            status.add("memory", memory);
        }

        {
            JsonObject application = new JsonObject();
            application.addProperty("uptime", Play.started ? System.currentTimeMillis() - Play.startedAt : -1);
            application.addProperty("path", Play.applicationPath.getAbsolutePath());
            status.add("application", application);
        }

        {
            JsonObject pool = new JsonObject();
            pool.addProperty("size", Invoker.executor.getPoolSize());
            pool.addProperty("active", Invoker.executor.getActiveCount());
            pool.addProperty("scheduled", Invoker.executor.getTaskCount());
            pool.addProperty("queue", Invoker.executor.getQueue().size());
            status.add("pool", pool);
        }

        return status;
    }

}
