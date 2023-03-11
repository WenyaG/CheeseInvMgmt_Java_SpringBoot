/** 
 * Name: Wenya Guo
 * Assignment: Java 3 Assignment 4
 * Program: Computer Programming
 * 
 * This project is for creating an application for a cheese factory.
 * In assignment4, we added the user registration form, and also moved the
 * database to a persistent storage system using MySQL server. 
 * 
 * This file is a java bean of Units class
 * 
 * @author guowen
 * 
 */

package ca.sheridancollege.guowen.assign.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
 
/**
 * Units for measuring cheese.
 * 
 * @author Wendi Jollymore
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Units {
    
    /**
     * The unique ID for this unit.
     * 
     * @param the ID for this unit
     * @return the unique ID for this unit
     */
    private int id;  // unique ID aka primary key
    
    /**
     * The name or description of this unit of measurement.
     * 
     * @param the new units description
     * @return this unit's description
     */
    private String description; // description of the units e.g. Lb. Wheel
    
}
