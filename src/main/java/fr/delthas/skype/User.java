package fr.delthas.skype;


import fr.delthas.skype.message.Message;

/**
 * A Skype account.
 * <p>
 * All information will be updated as updates are received (this object <b>is NOT</b> an immutable view/snapshot of a user account).
 */
public class User implements Chat {

  private final Skype skype;
  private final String username;
  private String firstname;
  private String lastname;
  private String mood;
  private String country;
  private String city;
  private String displayName;
  private Presence presence = Presence.OFFLINE;

  User(Skype skype, String username) {
    this.skype = skype;
    this.username = username;
  }

  /**
   * Blocks this user (without reporting the account).
   */
  public void block() {
    skype.block(this);
  }

  /**
   * Unblocks this user.
   */
  public void unblock() {
    skype.unblock(this);
  }

  /**
   * Sends a contact request to this account.
   *
   * @param greeting The message to send in the contact request.
   */
  public void sendContactRequest(String greeting) {
    skype.sendContactRequest(this, greeting);
  }

  /**
   * Removes this user from the list of contacts of the Skype account. If the user isn't a contact, nothing happens.
   */
  public void removeFromContacts() {
    skype.removeFromContacts(this);
  }

  /**
   * Sends a message to this user.
   *
   * @param message The message to send to this user.
   */
  @Deprecated
  public void sendMessage(String message) {
    skype.sendUserMessage(this, message);
  }

  @Override
  public void sendMessage(Message message) {
    skype.doMessageAction(this, message, Skype.MessageAction.SEND);
  }

  @Override
  public void editMessage(Message message) {
    skype.doMessageAction(this, message, Skype.MessageAction.EDIT);
  }

  @Override
  public void removeMessage(Message message) {
    skype.doMessageAction(this, message, Skype.MessageAction.REMOVE);
  }

  /**
   * @return The username of this user.
   */
  public String getUsername() {
    return username;
  }

  /**
   * @return The display name ("pretty" name) of this user.
   */
  public String getDisplayName() {
    if (displayName != null) {
      return displayName;
    }
    if (firstname != null) {
      if (lastname != null) {
        return firstname + " " + lastname;
      }
      return firstname;
    }
    return username;
  }

  /**
   * @return The first name of this user, or null if not visible.
   */
  public String getFirstname() {
    return firstname;
  }

  /**
   * @return The last name of this user, or null if not visible.
   */
  public String getLastname() {
    return lastname;
  }

  /**
   * @return The mood (status) of this user, or null if not visible.
   */
  public String getMood() {
    return mood;
  }

  /**
   * @return Two letters identifying this user's country (e.g. us), or null if not visible.
   */
  public String getCountry() {
    return country;
  }

  /**
   * @return The city of this user, or null if not visible.
   */
  public String getCity() {
    return city;
  }

  /**
   * @return The avatar (account picture) of this user, as a byte array, or null if not visible.
   */
  public byte[] getAvatar() {
    return skype.getAvatar(this);
  }

  /**
   * @return The presence of this user
   * @see Presence
   */
  public Presence getPresence() {
    return presence;
  }

  void setFirstName(String firstname) {
    if (firstname == null || firstname.isEmpty())
      return;
    this.firstname = firstname;
  }

  void setLastName(String lastname) {
    if (lastname == null || lastname.isEmpty())
      return;
    this.lastname = lastname;
  }

  void setMood(String mood) {
    if (mood == null || mood.isEmpty())
      return;
    this.mood = mood;
  }

  void setCountry(String country) {
    if (country == null || country.isEmpty())
      return;
    this.country = country;
  }

  void setCity(String city) {
    if (city == null || city.isEmpty())
      return;
    this.city = city;
  }

  void setDisplayName(String displayName) {
    if (displayName == null || displayName.isEmpty())
      return;
    this.displayName = displayName;
  }

  void setPresence(String presenceString) {
    setPresence(Presence.getPresence(presenceString), true);
  }

  void setPresence(Presence presence) {
    setPresence(presence, true);
  }

  void setPresence(Presence presence, boolean triggerListeners) {
    if (presence != this.presence) {
      Presence oldPresence = this.presence;
      this.presence = presence;
      if (triggerListeners) {
        skype.userPresenceChanged(this, oldPresence, presence);
      }
    }
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (username == null ? 0 : username.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof User)) {
      return false;
    }
    User other = (User) obj;
    if (username == null) {
      if (other.username != null) {
        return false;
      }
    } else if (!username.equals(other.username)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "User: " + getUsername();
  }

  @Override
  public Skype getSkype() {
    return skype;
  }

  @Override
  public String getIdentity() {
    return username;
  }

  @Override
  public ChatType getType() {
    return ChatType.USER;
  }
}
