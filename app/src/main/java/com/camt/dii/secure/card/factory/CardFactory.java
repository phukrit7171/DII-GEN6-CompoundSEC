package com.camt.dii.secure.card.factory;

import com.camt.dii.secure.card.AccessCard;
import com.camt.dii.secure.card.CardIdentifier;
import com.camt.dii.secure.card.Permission;

/**
 * Factory interface for creating access cards.
 * <p>
 * This interface is the core component of the Factory Method design pattern implementation 
 * in the access control system. It defines the contract that all concrete card factories
 * must implement, allowing for different card creation strategies without modifying client code.
 * </p>
 * 
 * <h3>Factory Pattern Implementation</h3>
 * <p>
 * The Factory Pattern is used here to:
 * <ul>
 *   <li>Abstract the card creation process from client code</li>
 *   <li>Allow for different card creation strategies (standard, secure, etc.)</li>
 *   <li>Encapsulate card instantiation logic in specialized factory classes</li>
 *   <li>Support future extension with new card types without modifying client code</li>
 * </ul>
 * </p>
 * 
 * <h3>Concrete Factory Implementations</h3>
 * <ul>
 *   <li>{@link StandardCardFactory} - Creates basic access cards with minimal processing</li>
 *   <li>{@link SecureCardFactory} - Creates cards with enhanced security features including encryption</li>
 * </ul>
 * 
 * <h3>Usage Example</h3>
 * <pre>
 *   CardFactory factory = new StandardCardFactory();
 *   CardIdentifier id = new CardIdentifier("CARD-001", "ADMIN", LocalDateTime.now());
 *   Permission permission = new SimplePermission(floors, rooms);
 *   AccessCard card = factory.createCard(id, permission);
 * </pre>
 * 
 * @pattern Factory Method Pattern - Defines an interface for creating an object,
 *          but lets subclasses decide which class to instantiate. Factory Method
 *          lets a class defer instantiation to subclasses.
 * @version 1.0
 * @author Access Control System Team
 * @see AccessCard
 * @see StandardCardFactory
 * @see SecureCardFactory
 */
public interface CardFactory {
    
    /**
     * Creates a new access card with the specified identifier and permission.
     * <p>
     * This is the factory method that all concrete factory implementations must provide.
     * Different implementations may create different types of cards or apply different
     * processing to the card creation (such as encryption, validation, etc.)
     * </p>
     * 
     * @param identifier the identifier for the new card, containing serial number, issuer, and date
     * @param permission the permission to assign to the new card, defining access rights
     * @return the newly created access card, fully initialized and ready for use
     * @throws IllegalArgumentException if the provided identifier or permission is invalid
     * @throws NullPointerException if either parameter is null
     */
    AccessCard createCard(CardIdentifier identifier, Permission permission);
}
