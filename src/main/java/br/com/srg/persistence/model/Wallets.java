package br.com.srg.persistence.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="wallets")
public class Wallets implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name="wallet_id")
    private int walletId;
    @ManyToOne
    @JoinColumn(name="user_id")
    private Users user;
    @Column(name="amount")
    private double amount;
    @ManyToOne
    @JoinColumn(name="currency_id")
    private Currencies currency;

    public int getWalletId() {
        return walletId;
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Currencies getCurrency() {
        return currency;
    }

    public void setCurrency(Currencies currency) {
        this.currency = currency;
    }
}
