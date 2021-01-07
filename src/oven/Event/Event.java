package oven.Event;

public class Event {
    public static EventType eventType;
    public static EventTiming eventTiming;
    public static String eventName;

    public Event(String name, EventType type, EventTiming timing){
        eventName = name;
        eventType = type;
        eventTiming = timing;
    }

    public static String getEventName() {
        return eventName;
    }
    public static EventType getEventType() {
        return eventType;
    }
    public static EventTiming getEventTiming() {
        return eventTiming;
    }
}
