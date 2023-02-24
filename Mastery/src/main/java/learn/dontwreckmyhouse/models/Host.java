package learn.dontwreckmyhouse.models;

import java.math.BigDecimal;

public class Host {
    private String hostId;
    private String hostLastName;
    private String hostEmail;
    private String hostPhone;
    private String hostAddress;
    private String hostCity;
    private String hostState;
    private int hostPostalCode;
    private BigDecimal standardRate;
    private BigDecimal weekendRate;

    public String getHostId() {return hostId;}

    public String getHostLastName() {return hostLastName;}

    public String getHostEmail() {return hostEmail;}

    public String getHostPhone() {return hostPhone;}

    public String getHostAddress() {return hostAddress;}

    public String getHostCity() {return hostCity;}

    public String getHostState() {return hostState;}

    public int getHostPostalCode() {return hostPostalCode;}

    public BigDecimal getStandardRate() {return standardRate;}

    public BigDecimal getWeekendRate() {return weekendRate;}

    public void setHostId(String hostId) {this.hostId = hostId;}

    public void setHostLastName(String hostLastName) {this.hostLastName = hostLastName;}

    public void setHostEmail(String hostEmail) {this.hostEmail = hostEmail;}

    public void setHostPhone(String hostPhone) {this.hostPhone = hostPhone;}

    public void setHostAddress(String hostAddress) {this.hostAddress = hostAddress;}

    public void setHostCity(String hostCity) {this.hostCity = hostCity;}

    public void setHostState(String hostState) {this.hostState = hostState;}

    public void setHostPostalCode(int hostPostalCode) {this.hostPostalCode = hostPostalCode;}

    public void setStandardRate(BigDecimal standardRate) {this.standardRate = standardRate;}

    public void setWeekendRate(BigDecimal weekendRate) {this.weekendRate = weekendRate;}
}