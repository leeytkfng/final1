package com.example.demo.reservationservice.entitiy;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    // ✅ 유저 ID
    @Column(nullable = false, name = "u_id")
    private Long uId;

    // ✅ 예매 ID
    @Column(nullable = false, name = "r_id")
    private Long rId;

    // ✅ 결제 금액
    @Column(nullable = false)
    private int price;

    // ✅ 결제 상태
    @Column(nullable = false)
    private String status; // 예: PENDING, COMPLETED, FAILED

    // ✅ 생성 시간
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // ✅ 3일 경과 확인용
    public boolean isExpired() {
        return createdAt != null && createdAt.isBefore(LocalDateTime.now().minusDays(3));
    }
}
