/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package courriel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Perfection
 */
public class CourrierModel {

	public enum CourrierPriorité {Pas_Urgent, Urgent}
	public enum CourrierState {Non_Traité, Traité}
	public enum CourrierPosition{Directeur, Ministère, SFE, Secrétaire, Ailleurs}
	
	private SimpleStringProperty name = new SimpleStringProperty();
    private SimpleStringProperty sendDate = new SimpleStringProperty();
    private SimpleStringProperty receivedNumberDB = new SimpleStringProperty();
    private SimpleStringProperty receivedDateDB = new SimpleStringProperty();
    private SimpleStringProperty senderName = new SimpleStringProperty();
    private SimpleStringProperty receivedDateSFE = new SimpleStringProperty();
    private SimpleStringProperty priority = new SimpleStringProperty();
    private SimpleStringProperty delay = new SimpleStringProperty();
    private SimpleStringProperty state = new SimpleStringProperty();
    private SimpleStringProperty position = new SimpleStringProperty();

    public CourrierModel(String name, String sendDate, String receivedNumberDB, String receivedDateDB, String senderName, String receivedDateSFE, String priority, String delay, String state, String position) {
        this.name.set(name);
        this.sendDate.set(sendDate);
        this.receivedNumberDB.set(receivedNumberDB);
        this.receivedDateDB.set(receivedDateDB);
        this.senderName.set(senderName);
        this.receivedDateSFE.set(receivedDateSFE);
        this.priority.set(priority);
        this.delay.set(delay);
        this.state.set(state);
        this.position.set(position);
    }
    
    public SimpleStringProperty getNameSimpleStringProperty(){
        return name;
    }
    public SimpleStringProperty getSendDateSimpleStringProperty(){
        return sendDate;
    }
    public SimpleStringProperty getReceivedNumberDBSimpleStringProperty(){
        return receivedNumberDB;
    }
    public SimpleStringProperty getReceivedDateDBSimpleStringProperty(){
        return receivedDateDB;
    }
    public SimpleStringProperty getSenderNumSimpleStringProperty(){
        return senderName;
    }
    public SimpleStringProperty getReceivedDateSFESimpleStringProperty(){
        return receivedDateSFE;
    }
    public SimpleStringProperty getPrioriSimpleStringProperty(){
        return priority;
    }
    public SimpleStringProperty getDelaySimpleStringProperty(){
        return delay;
    }
    public SimpleStringProperty getStateSimpleStringProperty(){
        return state;
    }
    public SimpleStringProperty getPositionSimpleStringProperty(){
        return position;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getSendDate() {
        return sendDate.get();
    }

    public void setSendDate(String sendDate) {
        this.sendDate.set(sendDate);
    }

    public String getReceivedNumberDB() {
        return receivedNumberDB.get();
    }

    public void setReceivedNumberDB(String receivedNumberDB) {
        this.receivedNumberDB.set(receivedNumberDB);
    }

    public String getReceivedDateDB() {
        return receivedDateDB.get();
    }

    public void setReceivedDateDB(String receivedDateDB) {
        this.receivedDateDB.set(receivedDateDB);
    }

    public String getSenderName() {
        return senderName.get();
    }

    public void setSenderName(String senderName) {
        this.senderName.set(senderName);
    }

    public String getReceivedDateSFE() {
        return receivedDateSFE.get();
    }

    public void setReceivedDateSFE(String receivedDateSFE) {
        this.receivedDateSFE.set(receivedDateSFE);
    }

    public String getPriority() {
        return priority.get();
    }

    public void setPriority(String priority) {
        this.priority.set(priority);
    }

    public String getDelay() {
        return delay.get();
    }

    public void setDelay(String delay) {
        this.delay.set(delay);
    }

    public String getState() {
        return state.get();
    }

    public void setState(String state) {
        this.state.set(state);
    }

    public String getPosition() {
        return position.get();
    }

    public void setPosition(String position) {
        this.position.set(position);
    }

    public String printInfos() {
        return ("Name: " + getName() + "\n"
                + "Send Date: " + getSendDate() + "\n"
                + "Rec Num DB: " + getReceivedDateDB() + "\n"
                + "Sender: " + getSenderName() + "\n"
                + "Rec Date SFE: " + getReceivedDateSFE() + "\n"
                + "Priority: " + getPriority() + "\n"
                + "Delay: " + getDelay() + "\n"
                + "State: " + getState() + "\n"
                + "Position: " + getPosition() + "\n");
    }

}
