/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.labplanet.modelo;

/**
 *
 * @author Administrator
 */
public class UserRole {
    
    private int userId;
    private String userRoleName;
    
    /**
     *
     */
    public UserRole(){
    
    }

    /**
     *
     * @param id user id
     * @param nombre userRoleName
     */
    public UserRole(int id, String nombre) {
        this.userId = id;
        this.userRoleName = nombre;        
    }

    /**
     *
     * @return the userId
     */
    public int getId() {
        return userId;
    }

    /**
     *
     * @return userRoleName
     */
    public String getNombre() {
        return userRoleName;
    }

    /**
     *
     * @param id userId
     */
    public void setId(int id) {
        this.userId = id;
    }

    /**
     *
     * @param nombre userRoleName
     */
    public void setUserRoleName(String nombre) {
        this.userRoleName = nombre;
    }

    /**
     *
     * @return a hashCode
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + this.userId;
        return hash;
    }

    /**
     *
     * @param obj the objectToCompare against the userRole instance
     * @return true-false when both object matches. false in case of any null one or when object is not a class Object.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserRole other = (UserRole) obj;
        return this.userId == other.userId;
    }
            
    
}
