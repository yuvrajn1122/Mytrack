package com.Wallet.Application.Entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.Wallet.Application.module.security.role.Role;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "apna_users")
public class User  implements UserDetails{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	private String firstName;

	private String lastName;
	
	private String password;

	private String otherName;

	private String gender;

	private String address;

	private String stateOfOrigin;

	private String accountNumber;

	private BigDecimal accountBalance;

	private String email;
	private Boolean emailVerified;
	private String phoneNumber;

	private Boolean phoneNumberVerified;
	
	
	private String alternativePhoneNumber;

	private String status;

	private Long referralId;
	@CreationTimestamp
	private LocalDateTime createdDateTime;

	@UpdateTimestamp
	private LocalDateTime modifiedAt;

	private Boolean isActive;
	private Role role;
	private BigDecimal dailyLimit;
    private BigDecimal spentToday;
    private LocalDate lastTransactionDate;
	@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Return the roles/authorities for the user
        return List.of(new SimpleGrantedAuthority("ROLE_USER")); // example role
    }
	@Override
    public String getUsername() {
        return this.email;  // Assuming email is used as username
    }
	

}
