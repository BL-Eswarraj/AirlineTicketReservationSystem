package exception;

public class UnauthorizedRoleException extends AirlineException {
    public UnauthorizedRoleException(String username, String role, String attemptedFeature) {
        super(String.format("Access Denied: User '%s' with role [%s] does not have permission to execute feature [%s].", username, role, attemptedFeature),
                "ERR_RBAC_403", 403);
    }
}