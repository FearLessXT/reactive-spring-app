package com.demo.reactive.entity.customer;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


@Builder
@Table("customer")
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
public class CustomerEntity {

    @Id
    @Column("customer_id")
    private Long id;

    @Column("first_name")
    private String firstName;

    @Column("last_name")
    private String lastName;

    @Column("gender")
    private String gender;

    @Column("wallet_number")
    private String walletNumber;

    private String dob;

    private String nationality;

    @Column("id_type")
    private String idType;

    @Column("id_number")
    private String idNumber;

    @Column("id_expiration_date")
    private String idExpirationDate;

    @Column("account_type")
    private String accountType;

    @Column("photo_of_id")
    private String photoOfId;

    @Column("selfie_photo")
    private String selfiePhoto;

    private String pin;
}
