package phong;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

interface CalendarSubject {
    void addObserver(CalendarObserver observer);
    void removeObserver(CalendarObserver observer);
    void notifyObservers();
}

interface CalendarObserver {
    void update(List<CalendarEvent> events);
}

class CalendarEvent {
    private String eventCode;
    private String eventName;
    private String location;
    private Date startTime;
    private Date endTime;
    private boolean isImportant;

    public CalendarEvent(String eventCode, String eventName, String location, Date startTime, Date endTime, boolean isImportant) {
        this.eventCode = eventCode;
        this.eventName = eventName;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isImportant = isImportant;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public boolean isImportant() {
        return isImportant;
    }

    public void setImportant(boolean important) {
        isImportant = important;
    }

    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String startTimeStr = dateFormat.format(startTime);
        String endTimeStr = dateFormat.format(endTime);
        String importantStatus = isImportant ? "Important" : "Not Important";
        return "Event: " + eventName + ", Location: " + location + ", Time: " + startTimeStr + " - " + endTimeStr
                + ", " + importantStatus + ", Event Code: " + eventCode;
    }
}

class CalendarObservable implements CalendarSubject {
    private List<CalendarObserver> observers = new ArrayList<>();
    private List<CalendarEvent> events = new ArrayList<>();

    public void addObserver(CalendarObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(CalendarObserver observer) {
        observers.remove(observer);
    }

    public void addEvent(CalendarEvent event) {
        events.add(event);
        notifyObservers();
    }

    public void removeEvent(CalendarEvent event) {
        events.remove(event);
        notifyObservers();
    }

    public void notifyObservers() {
        for (CalendarObserver observer : observers) {
            observer.update(events);
        }
    }
}

class CalendarManagerBase implements CalendarObserver {
    protected List<CalendarEvent> currentEvents = new ArrayList<>();

    public void update(List<CalendarEvent> events) {
        this.currentEvents = events;
    }

    public void displayEvents() {
        System.out.println("Current Events:");
        for (CalendarEvent event : currentEvents) {
            System.out.println(event.toString());
        }
    }

    public void displayEventsByDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateStr = dateFormat.format(date);

        List<CalendarEvent> eventsByDate = currentEvents.stream()
                .filter(event -> dateFormat.format(event.getStartTime()).equals(dateStr)
                        || dateFormat.format(event.getEndTime()).equals(dateStr))
                .collect(Collectors.toList());

        System.out.println("Events on " + dateStr + ":");
        for (CalendarEvent event : eventsByDate) {
            System.out.println(event.toString());
        }
    }

    public List<CalendarEvent> searchEventsByName(String name) {
        List<CalendarEvent> searchResults = currentEvents.stream()
                .filter(event -> event.getEventName().contains(name))
                .collect(Collectors.toList());

        return searchResults;
    }

    public void markEventAsImportant(String eventCode) {
        CalendarEvent event = findEventByCode(eventCode);
        if (event != null) {
            event.setImportant(true);
        } else {
            System.out.println("Event not found.");
        }
    }

    public void unmarkEventAsImportant(String eventCode) {
        CalendarEvent event = findEventByCode(eventCode);
        if (event != null) {
            event.setImportant(false);
        } else {
            System.out.println("Event not found.");
        }
    }

    public CalendarEvent findEventByCode(String eventCode) {
        for (CalendarEvent event : currentEvents) {
            if (event.getEventCode().equals(eventCode)) {
                return event;
            }
        }
        return null;
    }

    public void displayEventsWithinTimeRange(Date startTime, Date endTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        System.out.println("Events within the time range:");
        for (CalendarEvent event : currentEvents) {
            if (event.getStartTime().compareTo(startTime) >= 0 && event.getEndTime().compareTo(endTime) <= 0) {
                System.out.println(event.toString());
            }
        }
    }
}

class CalendarManager extends CalendarManagerBase {
    public void addEvent(String eventCode, String eventName, String location, Date startTime, Date endTime, boolean isImportant) {
        CalendarEvent event = new CalendarEvent(eventCode, eventName, location, startTime, endTime, isImportant);
        currentEvents.add(event);
    }

    public void removeEvent(CalendarEvent event) {
        currentEvents.remove(event);
    }

    public void editEvent(CalendarEvent event, String eventName, String location, Date startTime, Date endTime) {
        event.setEventName(eventName);
        event.setLocation(location);
        event.setStartTime(startTime);
        event.setEndTime(endTime);
    }

    public void displayEventsWithinTimeRange(Date startTime, Date endTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        System.out.println("Events within the time range:");
        for (CalendarEvent event : currentEvents) {
            if (event.getStartTime().compareTo(startTime) >= 0 && event.getEndTime().compareTo(endTime) <= 0) {
                System.out.println(event.toString());
            }
        }
    }

    // ... (các phương thức khác)
}

public class Main {
    public static void main(String[] args) {
        CalendarObservable calendar = new CalendarObservable();
        CalendarManager manager = new CalendarManager();
        calendar.addObserver(manager);

        Scanner scanner = new Scanner(System.in);

        int choice;
        do {
            System.out.println("--------- Calendar Menu ---------");
            System.out.println("1. Add Event");
            System.out.println("2. Remove Event");
            System.out.println("3. Edit Event");
            System.out.println("4. Display All Events");
            System.out.println("5. Display Events by Date");
            System.out.println("6. Search Events by Name");
            System.out.println("7. Mark Event as Important");
            System.out.println("8. Unmark Event as Important");
            System.out.println("9. Display Events Within Time Range");
            System.out.println("10. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    System.out.print("Enter event code: ");
                    String eventCode = scanner.nextLine();
                    System.out.print("Enter event name: ");
                    String eventName = scanner.nextLine();
                    System.out.print("Enter location: ");
                    String location = scanner.nextLine();
                    System.out.print("Enter start time (dd/MM/yyyy HH:mm:ss): ");
                    String startTimeStr = scanner.nextLine();
                    Date startTime = parseDate(startTimeStr);
                    if (startTime == null) {
                        System.out.println("Invalid date format. Please use dd/MM/yyyy HH:mm:ss.");
                        break;
                    }
                    System.out.print("Enter end time (dd/MM/yyyy HH:mm:ss): ");
                    String endTimeStr = scanner.nextLine();
                    Date endTime = parseDate(endTimeStr);
                    if (endTime == null) {
                        System.out.println("Invalid date format. Please use dd/MM/yyyy HH:mm:ss.");
                        break;
                    }
                    System.out.print("Is the event important? (true/false): ");
                    boolean isImportant = scanner.nextBoolean();
                    manager.addEvent(eventCode, eventName, location, startTime, endTime, isImportant);
                    break;
                case 2:
                    System.out.print("Enter event code to remove: ");
                    String eventCodeToRemove = scanner.nextLine();
                    CalendarEvent eventToRemove = manager.findEventByCode(eventCodeToRemove);
                    if (eventToRemove != null) {
                        manager.removeEvent(eventToRemove);
                        System.out.println("Event removed.");
                    } else {
                        System.out.println("Event not found.");
                    }
                    break;
                case 3:
                    System.out.print("Enter event code to edit: ");
                    String eventCodeToEdit = scanner.nextLine();
                    CalendarEvent eventToEdit = manager.findEventByCode(eventCodeToEdit);
                    if (eventToEdit != null) {
                        System.out.print("Enter new event name: ");
                        String newEventName = scanner.nextLine();
                        System.out.print("Enter new location: ");
                        String newLocation = scanner.nextLine();
                        System.out.print("Enter new start time (dd/MM/yyyy HH:mm:ss): ");
                        String newStartTimeStr = scanner.nextLine();
                        Date newStartTime = parseDate(newStartTimeStr);
                        if (newStartTime == null) {
                            System.out.println("Invalid date format. Please use dd/MM/yyyy HH:mm:ss.");
                            break;
                        }
                        System.out.print("Enter new end time (dd/MM/yyyy HH:mm:ss): ");
                        String newEndTimeStr = scanner.nextLine();
                        Date newEndTime = parseDate(newEndTimeStr);
                        if (newEndTime == null) {
                            System.out.println("Invalid date format. Please use dd/MM/yyyy HH:mm:ss.");
                            break;
                        }
                        manager.editEvent(eventToEdit, newEventName, newLocation, newStartTime, newEndTime);
                        System.out.println("Event edited.");
                    } else {
                        System.out.println("Event not found.");
                    }
                    break;
                case 4:
                    manager.displayEvents();
                    break;
                case 5:
                    System.out.print("Enter date to display events (dd/MM/yyyy): ");
                    String dateStr = scanner.nextLine();
                    Date dateToDisplay = parseDate(dateStr);
                    if (dateToDisplay == null) {
                        System.out.println("Invalid date format. Please use dd/MM/yyyy.");
                        break;
                    }
                    manager.displayEventsByDate(dateToDisplay);
                    break;
                case 6:
                    System.out.print("Enter event name to search: ");
                    String searchName = scanner.nextLine();
                    List<CalendarEvent> searchResults = manager.searchEventsByName(searchName);
                    if (!searchResults.isEmpty()) {
                        System.out.println("Search Results:");
                        for (CalendarEvent event : searchResults) {
                            System.out.println(event.toString());
                        }
                    } else {
                        System.out.println("No events found.");
                    }
                    break;
                case 7:
                    System.out.print("Enter event code to mark as important: ");
                    String importantEventCode = scanner.nextLine();
                    manager.markEventAsImportant(importantEventCode);
                    break;
                case 8:
                    System.out.print("Enter event code to unmark as important: ");
                    String unimportantEventCode = scanner.nextLine();
                    manager.unmarkEventAsImportant(unimportantEventCode);
                    break;
                case 9:
                    System.out.print("Enter start time of the range (dd/MM/yyyy HH:mm:ss): ");
                    String rangeStartTimeStr = scanner.nextLine();
                    Date rangeStartTime = parseDate(rangeStartTimeStr);
                    if (rangeStartTime == null) {
                        System.out.println("Invalid date format. Please use dd/MM/yyyy HH:mm:ss.");
                        break;
                    }
                    System.out.print("Enter end time of the range (dd/MM/yyyy HH:mm:ss): ");
                    String rangeEndTimeStr = scanner.nextLine();
                    Date rangeEndTime = parseDate(rangeEndTimeStr);
                    if (rangeEndTime == null) {
                        System.out.println("Invalid date format. Please use dd/MM/yyyy HH:mm:ss.");
                        break;
                    }
                    manager.displayEventsWithinTimeRange(rangeStartTime, rangeEndTime);
                    break;
                case 10:
                    System.out.println("Exiting the Calendar.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        } while (choice != 10);

        scanner.close();
    }

    private static Date parseDate(String dateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }
}
