package am.shared.dto;

import am.shared.enums.notification.AMEventNotifications;
import am.shared.enums.notification.AMEvents;
import am.shared.enums.notification.Attributes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ahmed.motair on 1/13/2018.
 */
public class AMNotification implements Serializable{
    private AMEvents event;
    private String categoryRelatedID;
    private Map<AMEventNotifications, List<AMDestination>> destinations;
    private Map<Attributes, String> parameters;

    public AMNotification() {
    }

    public AMNotification(AMEvents event, String categoryRelatedID, Map<AMEventNotifications, List<AMDestination>> destinations, Map<Attributes, String> parameters) {
        this.event = event;
        this.categoryRelatedID = categoryRelatedID;
        this.destinations = destinations;
        this.parameters = parameters;
    }

    public AMEvents getEvent() {
        return event;
    }
    public void setEvent(AMEvents event) {
        this.event = event;
    }

    public String getCategoryRelatedID() {
        return categoryRelatedID;
    }
    public void setCategoryRelatedID(String categoryRelatedID) {
        this.categoryRelatedID = categoryRelatedID;
    }

    public Map<AMEventNotifications, List<AMDestination>> getDestinations() {
        return destinations;
    }
    public List<AMDestination> getDestinations(AMEventNotifications eventNotification) {
        List<AMDestination> addressList = this.destinations.get(eventNotification);
        if(addressList != null)
            return addressList;
        else
            return new ArrayList<>();
    }
    public void setDestinations(Map<AMEventNotifications, List<AMDestination>> destinations) {
        this.destinations = destinations;
    }

    public Map<Attributes, String> getParameters() {
        return parameters;
    }
    public void setParameters(Map<Attributes, String> parameters) {
        this.parameters = parameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AMNotification)) return false;

        AMNotification that = (AMNotification) o;

        if (getEvent() != that.getEvent()) return false;
        if (getDestinations() != null ? !getDestinations().equals(that.getDestinations()) : that.getDestinations() != null) return false;
        return getParameters() != null ? getParameters().equals(that.getParameters()) : that.getParameters() == null;
    }

    @Override
    public int hashCode() {
        int result = getEvent() != null ? getEvent().hashCode() : 0;
        result = 31 * result + (getDestinations() != null ? getDestinations().hashCode() : 0);
        result = 31 * result + (getParameters() != null ? getParameters().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AMNotification{" +
                "event = " + event +
                ", addresses = " + destinations +
                ", parameters = " + parameters +
                "}\n";
    }
}
