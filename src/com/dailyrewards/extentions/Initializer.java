package com.dailyrewards.extentions;

import com.dailyrewards.PluginClass;
import com.dailyrewards.PluginLib;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public interface Initializer<T extends Initializer> {

    T onEnable();

    void onDisable();

    void onReload();

    static void unload(Object instance) {
        for (Field field : instance.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                field.set(instance, null);
            } catch (Exception ignored) {}
        }
    }

    static void registerInitializer(Initializer listener) {
        PluginClass.getPlugin().registerInitializer(listener);
    }

    static void init(Initializer.Action action, List<Initializer> listeners) {
        init(action, listeners.toArray(new Initializer[0]));
    }

    static void init(Initializer.Action action, Initializer... listener) {
        for (int x = 0; x < listener.length; x++) {
            try {
                switch (action) {
                    case ENABLE:
                        listener[x].onEnable();
                        break;
                    case DISABLE:
                        listener[x].onDisable();
                        break;
                    case RELOAD:
                        listener[x].onReload();
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static List<Initializer> getInitializerFields(Object instance) {
        Map<Initializer, PluginLib.LoadPriority.Priority> list = new LinkedHashMap<>();
        Object object;
        for (Field field : instance.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                object = field.get(instance);
                if (object instanceof Initializer) {
                    if (field.isAnnotationPresent(PluginLib.LoadPriority.class)) {
                        list.put((Initializer) object, field.getAnnotation(PluginLib.LoadPriority.class).priority());
                    } else {
                        list.put((Initializer) object, PluginLib.LoadPriority.Priority.LOW);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        TreeMap<Initializer, PluginLib.LoadPriority.Priority> sorted = new TreeMap<>(new ValueComparator(list));
        sorted.putAll(list);
        return new ArrayList<>(sorted.keySet());
    }

    class ValueComparator implements Comparator<Initializer> {

        private final Map<Initializer, PluginLib.LoadPriority.Priority> map;

        public ValueComparator(Map<Initializer, PluginLib.LoadPriority.Priority> map) {
            this.map = map;
        }

        @Override
        public int compare(Initializer o1, Initializer o2) {
            return (map.get(o1).getType() > map.get(o2).getType() ? -1 : 1);
        }
    }

    enum Action {
        ENABLE,
        DISABLE,
        RELOAD
    }
}
