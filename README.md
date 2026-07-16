# Airline Ticket Reservation System


A production-ready, enterprise-grade **Airline Ticket Reservation System** implemented in pure **Java 17+**. This project models end-to-end civil aviation operations—from multi-tier user role hierarchies and interactive ASCII seat mapping to algorithmic surge pricing, Air Traffic Control (ATC) telemetry validation, and sub-millisecond network route indexing.

The system is architected around **SOLID design principles**, incorporates **Gang of Four (GoF) Design Patterns**, implements custom **Data Structures & Algorithms (DSA)** for high-performance computing, and takes full advantage of modern Java language features such as **Records, Text Blocks, Pattern Matching, and the Stream API**.

---

## Architectural & Technical Highlights

| Pattern / Technology | Module(s) | Implementation Details |
| :--- | :--- | :--- |
| **State Pattern** | UC04 | Strictly governs booking transitions through `INITIATED` → `PASSENGER_DETAILS` → `SEAT_SELECTED` → `PAYMENT_PENDING` → `CONFIRMED`/`CANCELLED`. |
| **Strategy Pattern** | UC05, UC12, UC13 | Hot-swaps payment gateways (UPI, Card, EMI), notification channels (Email, SMS, WhatsApp), and report exporters (PDF, CSV). |
| **Observer Pattern** | UC12 | Subscribes passengers to live flight events and automatically broadcasts real-time delay and boarding alerts to all subscribers. |
| **Rule Engine Pattern** | UC16 | Decouples complex pricing logic into a sequential pipeline (Advance Discounts → Festive Surge → Excess Baggage → Tiered GST). |
| **Adapter Pattern** | UC18 | Translates internal domain calls into external REST JSON payloads with exponential backoff retries and ATC telemetry checks. |
| **Singleton Patterns** | UC11 | Implements Double-Checked Locking (DB Pool), Bill Pugh Static Inner Class (System Config), and Enum Singleton (Memory Cache). |
| **PriorityQueue (DSA)** | UC10 | Sorts booking requests by `EXPRESS` vs. `REGULAR` tiers with a FIFO timestamp tie-breaker and an aging algorithm to prevent starvation. |
| **Graph Adjacency List (DSA)** | UC15 | Models airport hubs as `Map<String, List<Flight>>` to discover 1-stop connecting routes in near-linear time **O(K)** without database scans. |
| **LRU Cache + TTL (DSA)** | UC15 | Wraps `LinkedHashMap` and `ConcurrentHashMap` with access-order eviction and Time-To-Live expiration for sub-millisecond lookups. |
| **Java Stream API** | UC02, UC13 | Performs multi-criteria filtering and aggregations using `Collectors.groupingBy`, `minBy`, `summingInt`, and `averagingDouble`. |

---

## Complete Module Breakdown (18 Use Cases)

### 1. User Management (UC01)
* **Registration & Auth:** SHA-256 password encryption, OTP simulation for email/mobile verification, session token management, and "Remember Me" functionality.
* **Profile Management:** Stores passport documents, emergency contacts, travel preferences (meal/seat), and linked companion profiles.
* **Polymorphic RBAC:** Abstract `User` hierarchy extended by `Passenger`, `Admin`, and `AirlineStaff`, overriding feature-level access permissions.

### 2. Flight Search & Discovery (UC02)
* **Search & Filter:** Searches by route and date, filtering by stops, price, airline, and departure time slots.
* **Stream Aggregations:** Groups flights by airline, price range (`BUDGET`, `MODERATE`, `PREMIUM`), and time slot (`MORNING`, `AFTERNOON`, `EVENING`, `NIGHT`), calculating average fares and identifying cheapest routes via `minBy`.

### 3. Seat Selection (UC03)
* **ASCII Interactive Map:** Renders live terminal seat maps with ANSI color coding (Green: Available, Red: Booked, Yellow: Locked, Gray: Blocked).
* **Safety Restrictions:** Enforces age ($18+$) and physical eligibility validation before allowing Emergency Exit row selection.
* **Group Auto-Assign:** Algorithmically scans rows to assign adjacent consecutive seats for family and group bookings.

### 4. Booking Management (UC04)
* **State Lifecycle:** Prevents illegal operations (e.g., paying before selecting seats) via clean state transitions.
* **PNR Generation:** Generates unique 6-character PNR references and temporary 15-minute payment seat locks.
* **History & Export:** Displays passenger booking histories and simulates generating downloadable PDF E-Tickets.

### 5. Payment Processing (UC05)
* **Indian Payment Strategies:** Implements UPI (Google Pay, PhonePe, Paytm, BHIM with regex validation), PCI-DSS masked Credit/Debit Cards, and EMI tenure conversions.
* **Security & Idempotency:** Prevents duplicate transaction submissions and calculates promotional discount codes.
* **Refund Engine:** Automatically calculates refund deductions based on cancellation policy rules.

### 6. Booking Modification (UC06)
* **Flight & Seat Changes:** Computes fare differences and seat tier upgrade surcharges, releasing old seat allocations back to inventory.
* **Anti-Fraud Name Corrections:** Allows typo corrections only when verified against originally registered ID document numbers.

### 7. Booking Cancellation (UC07)
* **Policy Management:** Evaluates time-to-departure (`ChronoUnit.HOURS`) and ticket tier (`NON_REFUNDABLE`, `STANDARD`, `FLEXIBLE`) to apply tiered cancellation penalties.
* **Partial Group Cancellations:** Allows cancelling individual passengers within a PNR, recalculating refunds, and re-issuing e-tickets for remaining travelers.

### 8. Flight Management (UC08 - Admin/Staff)
* **Dynamic Surge Pricing:** Automatically adjusts base fares using algorithmic demand multipliers when class occupancy exceeds $80\%$ or drops below $30\%$, while applying seasonal holiday surge multipliers.
* **Operations & Delays:** Updates live operational statuses (`ON_TIME`, `DELAYED`, `CANCELLED`) and broadcasts notifications.

### 9. Airport Management (UC09)
* **Infrastructure Tracking:** Manages multi-terminal gate layouts, timezones, operational status toggles, and facility directories (VIP Lounges, Medical Centers, Duty-Free).
* **Auto-Suggest Engine:** Matches user prefix strings against IATA codes, cities, and airport names during flight searches.

### 10. Priority Booking Queue (UC10 - DSA)
* **Express Processing:** Uses Java's `PriorityQueue` to process Express fee-paying passengers ahead of Regular requests.
* **Starvation Protection:** Implements an aging algorithm that elevates Regular bookings to Express priority if queue waiting time exceeds threshold limits.

### 11. Singleton Managers (UC11)
* **DatabaseConnectionManager:** Double-checked locking thread-safe connection pool.
* **SystemConfigurationManager:** Bill Pugh static inner class managing global GST rates and maintenance toggles.
* **SystemCacheManager:** Joshua Bloch enum singleton managing concurrent in-memory caching.

### 12. Notification System (UC12)
* **Strategy & Observer:** Passengers subscribe to flight numbers; when staff publishes a delay or boarding event, the dispatcher routes personalized messages across preferred channels (Email, SMS, WhatsApp).

### 13. Reporting & Analytics (UC13)
* **Executive Analytics:** Compiles fleet-wide occupancy rates and gross/net financial revenue reports.
* **Runtime Exporters:** Swappable strategies to output data as formatted ASCII PDF reports or raw CSV dumps.

### 14. Online Check-In (UC14)
* **Time-Window Enforcement:** Restricts web check-in strictly to between 48 hours and 1 hour before scheduled departure.
* **Digital Boarding Pass:** Enforces dangerous goods safety declarations, assigns boarding zones, and generates cryptographic ASCII barcodes.

### 15. Search Optimization (UC15 - DSA)
* **Graph Route Indexer:** Models network flights as an adjacency list graph, discovering 1-stop connecting flights ($A \to B \to C$) in linear time $O(K)$.
* **LRU & TTL Cache:** Custom cache that evicts least recently used search keys when at capacity and automatically purges stale data after Time-To-Live expiration.

### 16. Business Rules & Fare Calculation (UC16)
* **Sequential Rule Engine:** Decouples pricing into modular rules: Advance Reservation Discounts ($5\%–10\%$), Festival Surges ($15\%$), Excess Baggage Penalties (₹500/kg), and Indian Civil Aviation GST ($5\%–12\%$).

### 17. Exception Handling (UC17)
* **Domain Exception Hierarchy:** Custom runtime exceptions (`InvalidPNRException`, `SeatUnavailableException`, `PaymentFailedException`, `UnauthorizedRoleException`).
* **Centralized Dispatcher:** Intercepts failures across service layers and converts them into structured, immutable `ErrorResponse` records with HTTP-style status codes and action hints.

### 18. Third-Party Integrations & REST APIs (UC18)
* **REST DTO Records:** Models JSON request/response payloads using Java Text Blocks.
* **Gateway Adapters & Backoff:** Translates internal calls to external payment and SMS gateways, implementing automatic exponential backoff retries during HTTP `503 Service Unavailable` network drops.
* **ATC Telemetry Validation:** Verifies external meteorological wind speed and airspace advisories before granting takeoff clearance.

---

## 📂 Project Directory Structure

```text
airline-reservation-system/
├── src/
│   ├── domain/
│   │   ├── airport/          # Airport, Terminal, FacilityType, AirportContact
│   │   ├── booking/          # BookingContext, BookingState, PassengerInfo
│   │   ├── checkin/          # BoardingPass, BaggageDeclaration, PassengerCheckInRecord
│   │   ├── flight/           # Flight, Fare, BaggagePolicy, SearchCriteria, RoundTripOption
│   │   ├── flightmanagement/ # ManagedFlight, ClassConfiguration, ScheduleType
│   │   ├── notification/     # NotificationSender, Email/Sms/WhatsApp strategies
│   │   ├── optimization/     # OptimizedFlight, RoutePath, SearchQueryKey, CachedSearchResult
│   │   ├── payment/          # PaymentMethod, UpiPayment, CardPayment, EmiPayment, PromoCode
│   │   ├── queue/            # PriorityBookingRequest, BookingPriorityLevel, QueueReport
│   │   ├── rules/            # FareCalculationContext, PassengerTravelDetails, FareBreakdown
│   │   ├── seat/             # Seat, SeatMap, SeatType, SeatStatus, SeatCategory
│   │   └── user/             # User, Passenger, Admin, AirlineStaff, TravelPreferences
│   │
│   ├── exception/            # AirlineException, GlobalExceptionHandler, ErrorResponse DTOs
│   │
│   ├── integration/
│   │   ├── adapter/          # ExternalPaymentGatewayAdapter, SmsAdapter, AtcWeatherAdapter
│   │   ├── client/           # MockRestHttpClient
│   │   └── dto/              # BankPaymentRestRequest/Response, WeatherAtcRestResponse
│   │
│   ├── manager/              # DatabaseConnectionManager, SystemConfigurationManager, SystemCacheManager
│   │
│   ├── menu/                 # CLI Menus (PassengerMenu, AdminMenu, StaffMenu)
│   │
│   ├── service/
│   │   ├── airport/          # AirportManagementService
│   │   ├── booking/          # BookingService
│   │   ├── cancellation/     # CancellationService, CancellationPolicy
│   │   ├── checkin/          # WebCheckInService, BoardingPassGenerator
│   │   ├── flight/           # FlightSearchService
│   │   ├── flightmanagement/ # FlightManagementService, DynamicPricingEngine
│   │   ├── modification/     # FlightChangeService, PassengerModificationService, SeatChangeService
│   │   ├── notification/     # NotificationDispatcher, FlightEventPublisher, PassengerAlertObserver
│   │   ├── optimization/     # OptimizedFlightSearchFacade, RouteIndexingService, FlightCacheEngine
│   │   ├── payment/          # PaymentProcessorService, RefundService
│   │   ├── queue/            # ExpressQueueManagerService
│   │   ├── report/           # AnalyticsEngineService, CsvReportExporter, PdfReportExporter
│   │   ├── rules/            # FareCalculationEngine, AdvanceDiscountRule, SurgeRule, GstRule
│   │   ├── seat/             # SeatSelectionService
│   │   └── user/             # UserService, UserSession, SecurityUtil
│   │
│   ├── util/                 # ScannerHelper
│   │
│   ├── AirlineReservationApp.java       # Main Interactive CLI Entry Point
│   ├── UserManagementDemo.java          # UC01 Verification Demo
│   ├── FlightSearchDemo.java            # UC02 Verification Demo
│   ├── SeatSelectionDemo.java           # UC03 Verification Demo
│   ├── BookingManagementDemo.java       # UC04 Verification Demo
│   ├── PaymentProcessingDemo.java       # UC05 Verification Demo
│   ├── BookingModificationDemo.java     # UC06 Verification Demo
│   ├── BookingCancellationDemo.java     # UC07 Verification Demo
│   ├── FlightManagementDemo.java        # UC08 Verification Demo
│   ├── AirportManagementDemo.java       # UC09 Verification Demo
│   ├── PriorityBookingDemo.java         # UC10 Verification Demo
│   ├── SingletonManagersDemo.java       # UC11 Verification Demo
│   ├── NotificationSystemDemo.java      # UC12 Verification Demo
│   ├── ReportingAnalyticsDemo.java      # UC13 Verification Demo
│   ├── OnlineCheckInDemo.java           # UC14 Verification Demo
│   ├── SearchOptimizationDemo.java      # UC15 Verification Demo
│   ├── BusinessRulesDemo.java           # UC16 Verification Demo
│   ├── ExceptionHandlingDemo.java       # UC17 Verification Demo
│   └── ThirdPartyIntegrationDemo.java   # UC18 Verification Demo
│
└── README.md
