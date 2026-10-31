package com.example.hackathon.hackathon.repository;

import com.example.hackathon.hackathon.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    @Query("SELECT a FROM Application a WHERE a.selected = true AND " +
           "(a.smsSent = false OR a.emailSent24Hrs = false OR a.emailSent48Hrs = false)")
    List<Application> findSelectedApplicationsWithPendingNotifications();

    @Query("SELECT a FROM Application a WHERE a.id = :id AND a.selected = true")
    Optional<Application> findSelectedById(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Application a SET a.smsSent = true WHERE a.id = :id")
    int markSmsAsSent(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Application a SET a.emailSent24Hrs = true WHERE a.id = :id")
    int markEmail24HrsAsSent(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Application a SET a.emailSent48Hrs = true WHERE a.id = :id")
    int markEmail48HrsAsSent(@Param("id") Long id);

    @Query("SELECT a FROM Application a WHERE a.selected = true AND a.smsSent = false")
    List<Application> findPendingSmsNotifications();

    @Query("SELECT a FROM Application a WHERE a.selected = true AND a.emailSent24Hrs = false AND a.selectedAt <= :cutoffTime")
    List<Application> findPending24HrEmailNotifications(@Param("cutoffTime") LocalDateTime cutoffTime);

    @Query("SELECT a FROM Application a WHERE a.selected = true AND a.emailSent48Hrs = false AND a.selectedAt <= :cutoffTime")
    List<Application> findPending48HrEmailNotifications(@Param("cutoffTime") LocalDateTime cutoffTime);
}
