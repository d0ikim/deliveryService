# deliveryService

사장님이 메뉴를 올리고, 손님이 주문·결제하고, 사장님이 주문을 처리하는 작은 배달 주문 서비스 백엔드 API입니다.

## 기술 스택

- Java 21
- Spring Boot 4.1.x (Web, Data JPA, Security, Validation)
- PostgreSQL 18
- JWT (jjwt)
- Gradle

## 실행 방법

### 1. PostgreSQL 준비 (Docker)

```bash
docker run --name delivery-db \
  -e POSTGRES_USER=delivery \
  -e POSTGRES_PASSWORD=delivery1234 \
  -e POSTGRES_DB=delivery \
  -p 5432:5432 \
  -d postgres:18
```

### 2. 애플리케이션 실행

```bash
./gradlew bootRun
```

`src/main/resources/application.yml`에서 DB 접속 정보를 확인/수정할 수 있습니다.

## ERD

```mermaid
erDiagram
    USERS ||--o{ MENUS : "소유 (owner)"
    USERS ||--o{ ORDERS : "주문 (customer)"
    MENUS ||--o{ ORDERS : "주문됨"
    ORDERS ||--o{ PAYMENTS : "결제됨"

    USERS {
        bigint id PK
        varchar username UK "4~20자"
        varchar password "BCrypt 해시"
        varchar role "CUSTOMER / OWNER"
        timestamp created_at
        timestamp updated_at
    }

    MENUS {
        bigint id PK
        bigint owner_id FK "-> users.id"
        varchar name
        int price
        varchar description
        boolean deleted "Soft Delete"
        timestamp created_at
        timestamp updated_at
    }

    ORDERS {
        bigint id PK
        bigint customer_id FK "-> users.id"
        bigint menu_id FK "-> menus.id"
        int quantity
        int total_price "서버 계산"
        varchar delivery_address
        varchar status "ORDERED/PAID/ACCEPTED/COMPLETED/CANCELED"
        timestamp created_at
        timestamp updated_at
    }

    PAYMENTS {
        bigint id PK
        bigint order_id FK "-> orders.id"
        int amount
        varchar method "CARD"
        varchar status "COMPLETED"
        timestamp created_at
        timestamp updated_at
    }
```

## 주문 상태 흐름

```mermaid
stateDiagram-v2
    [*] --> ORDERED : 주문 생성
    ORDERED --> PAID : 손님이 결제
    ORDERED --> CANCELED : 손님이 취소 (결제 전)
    PAID --> ACCEPTED : 사장님이 수락
    ACCEPTED --> COMPLETED : 사장님이 배달완료 처리
```

## 사용자 역할

| 역할 | 할 수 있는 것 |
| --- | --- |
| `CUSTOMER` | 메뉴 조회 · 주문 생성 · 주문 취소 · 결제 · 내 주문 조회 |
| `OWNER` | 메뉴 등록·수정·삭제 · 내 메뉴에 들어온 주문 조회 · 주문 상태 변경 |