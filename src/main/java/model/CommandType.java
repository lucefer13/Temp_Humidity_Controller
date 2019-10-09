package model;

public enum CommandType {
    STATUS("Get status of inner sensor and outer sensor"),
    FIND_COM("Find senors port number, and automatical set port number"),
    //   SETUP_ALERTS("Setup alert events"),
    //   SETUP_COM("Non automatical setup of port numbers"),
    //   SETUP_MAIL("Setup mail accaunt"),
    //   SETUP_SNMP("Setup SNMP protocol"),
    //   SETUP_FILE("File with alerts message"),
    COMMAND_LIST("This list");

    private final String title;

    CommandType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return super.name() + "\t" + title;
    }
}
