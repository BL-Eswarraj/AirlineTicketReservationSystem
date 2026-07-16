package domain.user;

import java.time.LocalDate;

public abstract class User {
    protected String userId;
    protected String name;
    protected String email;
    protected String phone;
    protected String passwordHash;
    protected LocalDate dateOfBirth;
    protected boolean isEmailVerified;
    protected boolean isPhoneVerified;
    protected boolean isMfaEnabled;
    protected boolean isActive;
    protected String rememberMeToken;
    protected UserRole role;

    public User(String userId, String name, String email, String phone, String passwordHash, LocalDate dateOfBirth, UserRole role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.passwordHash = passwordHash;
        this.dateOfBirth = dateOfBirth;
        this.role = role;
        this.isEmailVerified = false;
        this.isPhoneVerified = false;
        this.isMfaEnabled = true; // Enabled by default for multi-factor security
        this.isActive = true;
    }

    // 1.3 Polymorphic permission check
    public abstract boolean hasPermission(String feature);

    public void updateContactDetails(String phone, String email) {
        this.phone = phone;
        this.email = email;
    }

    public void setPasswordHash(String newPasswordHash) {
        this.passwordHash = newPasswordHash;
    }

    public void setRememberMeToken(String token) {
        this.rememberMeToken = token;
    }

    public void setVerified(boolean emailVerified, boolean phoneVerified) {
        this.isEmailVerified = emailVerified;
        this.isPhoneVerified = phoneVerified;
    }

    public void deactivateAccount() {
        this.isActive = false;
    }

    public void setMfaEnabled(boolean mfaEnabled) {
        this.isMfaEnabled = mfaEnabled;
    }

    // Getters
    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getPasswordHash() { return passwordHash; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public UserRole getRole() { return role; }
    public boolean isActive() { return isActive; }
    public boolean isMfaEnabled() { return isMfaEnabled; }
    public String getRememberMeToken() { return rememberMeToken; }
}