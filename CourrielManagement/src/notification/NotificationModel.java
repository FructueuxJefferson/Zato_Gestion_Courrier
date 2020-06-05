package notification;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Perfection
 */
public class NotificationModel {

	
	private SimpleStringProperty date = new SimpleStringProperty();
    private SimpleStringProperty name = new SimpleStringProperty();
    private SimpleStringProperty description = new SimpleStringProperty();

    public NotificationModel(String date, String name, String description) {
        this.date.set(date);
        this.name.set(name);
        this.description.set(description);
    }
    
    

    public String getDate() {
		return date.get();
	}



	public void setDate(SimpleStringProperty date) {
		this.date = date;
	}
	
	public void setDate(String date) {
		this.date.set(date);
	}



	public String getName() {
		return name.get();
	}



	public void setName(SimpleStringProperty name) {
		this.name = name;
	}
	
	public void setName(String name) {
		this.name.set(name);
	}



	public String getDescription() {
		return description.get();
	}



	public void setDescription(SimpleStringProperty description) {
		this.description = description;
	}

	public void setDescription(String description) {
		this.description.set(description);
	}



	public String printInfos() {
        return (" Le courrier du: " + getDate() + "\n"
                + "Portant le nom de: " + getName() + "\n"
                + getDescription() +"\n");
    }

}
