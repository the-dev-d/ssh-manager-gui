package sshmanager.service;

import sshmanager.utils.Model;

public class SSHDB extends Model{

    private Integer id;

    private String name;

    private String username; 

    private String ip;

    private Integer port;

    private Integer authType;

    private String password;

    private boolean active = false;

    //generate constructors 
    public SSHDB() {
    }

    public SSHDB(Integer id, String name, String ip, Integer port, Integer authType, String username, String password) {
        this.id = id;
        this.name = name;
        this.ip = ip;
        this.port = port;
        this.authType = authType;
        this.username = username;
        this.password = password;
    }


    //generate getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getAuthType() {
        return authType;
    }

    public void setAuthType(Integer authType) {
        this.authType = authType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCreateQuery() {
        return "CREATE TABLE IF NOT EXISTS ssh (id INTEGER PRIMARY KEY AUTO_INCREMENT, name VARCHAR(30), ip VARCHAR(20), port INTEGER, auth_type TINYINT, username VARCHAR(20), password VARCHAR(30))";
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return this.name;
    }
}
