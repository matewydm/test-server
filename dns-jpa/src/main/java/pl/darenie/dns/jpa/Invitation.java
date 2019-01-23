package pl.darenie.dns.jpa;

import pl.darenie.dns.jpa.superclass.TimestampEntity;
import pl.darenie.dns.model.enums.InvitationStatus;

import javax.persistence.*;

@Entity
@Table(name = "invitation")
public class Invitation extends TimestampEntity {

    @Id
    @GeneratedValue(generator="invitation_seq")
    @SequenceGenerator(name="invitation_seq", sequenceName="invitation_SEQ", allocationSize=1)
    private Long id;

    @ManyToOne
    @JoinColumn(referencedColumnName = "firebase_token", name = "sender")
    private User sender;

    @Column(name = "sender", updatable = false, insertable = false)
    private String senderToken;

    @ManyToOne
    @JoinColumn(referencedColumnName = "firebase_token", name = "receiver")
    private User receiver;

    @Column(name = "receiver", updatable = false, insertable = false)
    private String receiverToken;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private InvitationStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getSenderToken() {
        return senderToken;
    }

    public void setSenderToken(String senderToken) {
        this.senderToken = senderToken;
    }

    public String getReceiverToken() {
        return receiverToken;
    }

    public void setReceiverToken(String receiverToken) {
        this.receiverToken = receiverToken;
    }

    public InvitationStatus getStatus() {
        return status;
    }

    public void setStatus(InvitationStatus status) {
        this.status = status;
    }
}
