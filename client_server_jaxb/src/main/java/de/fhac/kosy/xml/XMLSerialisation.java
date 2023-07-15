package de.fhac.kosy.xml;

import de.fhac.kosy.xml.generated.EchoMessage;
import de.fhac.kosy.xml.generated.EchoMessageType;
import de.fhac.kosy.xml.generated.ObjectFactory;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.StringReader;
import java.io.StringWriter;


public class XMLSerialisation {
    private ObjectFactory of;
    private JAXBContext jc;
    private Marshaller marshaller;
    private Unmarshaller unmarshaller;
    private String sender;

    /**
     * Erstellt ein XMLSerialisation-Objekt, welches ein EchoMessage-Objekt in einen XML-String
     * serialisiert/deserialisiert
     * <p>
     * In der Methode werden die Klassenparameter initialisiert. Ausserdem wird der
     * formatierte Output zur besseren Lesbarkeit eingeschaltet.
     *
     * @param sender Sendername
     * @see JAXBContext, marshaller, unmarshaller
     */
    public XMLSerialisation(String sender) throws JAXBException {
        // initialize class-member
        this.sender = sender;
        this.of = new ObjectFactory();
        this.jc = JAXBContext.newInstance(EchoMessage.class);
        this.marshaller = jc.createMarshaller();
        this.marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        this.unmarshaller = jc.createUnmarshaller();
    }

    /**
     * Serialisiert das Objekt in die XML-Repraesentation.
     *
     * @param message Object das serialisiert werden soll
     * @return die XML-Repraesentation des Objekts als String
     * @see StringWriter, Marshaller.marshall()
     */
    public String messageToXMLString(EchoMessage message) throws JAXBException {
        // message->XML
        StringWriter stringWriter = new StringWriter();
        this.marshaller.marshal(message, stringWriter);
        return stringWriter.toString();
    }

    public EchoMessage getNewMessage() {
        // EchoMessage aus ObjectFactory erzeugen, Sender und Type setzen
        EchoMessage echoMessage = this.of.createEchoMessage();
        echoMessage.setSender(this.sender);
        echoMessage.setType(EchoMessageType.DEFAULT);
        return echoMessage;
    }

    /**
     * Deserialisiert von einem XML-String ein Objekt aus dem JAXBContext
     *
     * @param xml XML-Repraesentation eines EchoMessage-Objekts
     * @return EchoMessage-Object
     * @throws JAXBException
     * @see StringReader, Unmarshaller.unmarshall()
     */
    public EchoMessage xmlStringToMessage(String xml) throws JAXBException {
        // XML->message
        StringReader stringReader = new StringReader(xml);
        return (EchoMessage) this.unmarshaller.unmarshal(stringReader);
    }
}
