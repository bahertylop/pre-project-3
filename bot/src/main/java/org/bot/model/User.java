package org.bot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tg_users")
public class User {

    @Id
    @SequenceGenerator(name = "tg_users_id_seq", sequenceName = "tg_users_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tg_users_id_seq")
    private Long id;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "chat_id")
    private String chatId;

    @Column(name = "tg_username")
    private String thUserName;

    @Column(name = "jwt")
    private String jwtToken;

    @Column(name = "refresh")
    private String refreshToken;

    @Enumerated(EnumType.STRING)
    @Column(name = "bot_state")
    private BotState botState;

    public enum BotState {
        REGISTER;
    }
}
