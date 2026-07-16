package manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatabaseConnectionManager {
    // volatile ensures changes made in one thread are immediately visible to others
    private static volatile DatabaseConnectionManager instance;

    private final List<String> connectionPool;
    private final int MAX_POOL_SIZE = 5;

    // Private constructor prevents direct instantiation from outside
    private DatabaseConnectionManager() {
        System.out.println("[DB MANAGER INIT] Initializing Database Connection Pool...");
        this.connectionPool = Collections.synchronizedList(new ArrayList<>());

        // Seed connection pool
        for (int i = 1; i <= MAX_POOL_SIZE; i++) {
            connectionPool.add("DB-CONN-POOL-NODE-" + i);
        }
        System.out.println("[DB MANAGER INIT] Pool successfully seeded with " + MAX_POOL_SIZE + " active connections.");
    }

    // Thread-safe Double-Checked Locking Pattern
    public static DatabaseConnectionManager getInstance() {
        if (instance == null) { // First check (no locking overhead)
            synchronized (DatabaseConnectionManager.class) {
                if (instance == null) { // Second check (with lock)
                    instance = new DatabaseConnectionManager();
                }
            }
        }
        return instance;
    }

    public synchronized String borrowConnection(String requesterName) {
        if (connectionPool.isEmpty()) {
            System.out.printf("[DB WARNING] %s requested connection, but pool is empty! Waiting for release...%n", requesterName);
            return null;
        }
        String conn = connectionPool.remove(0);
        System.out.printf("[DB BORROW] %s checked out [%s]. Remaining pool size: %d%n", requesterName, conn, connectionPool.size());
        return conn;
    }

    public synchronized void releaseConnection(String connectionName, String releaserName) {
        if (connectionName != null && connectionPool.size() < MAX_POOL_SIZE) {
            connectionPool.add(connectionName);
            System.out.printf("[DB RELEASE] %s returned [%s] to pool. Current pool size: %d%n", releaserName, connectionName, connectionPool.size());
        }
    }

    public int getAvailableConnectionsCount() {
        return connectionPool.size();
    }
}