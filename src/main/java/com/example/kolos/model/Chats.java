package com.example.kolos.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;



@Entity
@Table(name="chats")
public class Chats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_chat")
    private Long idChat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="founder_chat")
    private User founderChat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="members")
    private User members;

    public Chats(){}
    
}
