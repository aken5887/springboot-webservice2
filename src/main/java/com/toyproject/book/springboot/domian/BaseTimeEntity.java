package com.toyproject.book.springboot.domian;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass   //JPA Entity 클래스들이 해당 클래스를 상속할 경우 클래스내 필드도 칼럼으로 인식
@EntityListeners(AuditingEntityListener.class) // Auditing 기능 포함(Spring Data JPA에서 시간에 대해서 자동으로 값을 넣어주는 기능)
public class BaseTimeEntity {
    @CreatedDate
    private LocalDateTime createdDate;
    @LastModifiedDate
    private LocalDateTime modifiedDate;
}
