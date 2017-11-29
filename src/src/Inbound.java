package src;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;

public class Inbound {
	private String xml;
	
	private final static DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

	private String timeStamp;
	private String pushId;
	private String inboundId;
	private String subscriptionUid;
	private String phoneNumber;
	private String contents;
	private String keyword;
	private String dataCapture;
	private String attachment;
	private String datasetId;
	private String datasetName;
	
	public Inbound(String xml) throws Exception {
		this.xml = xml;
		if (getValueFromExpression(xml, "TRUMPIA").isEmpty()) {
			return;
		}
		
		parsing(xml);
		
		String trumpiaLog = "c:/documents/TrumpiaLog.csv";
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(trumpiaLog), "MS949"));
		writer.write("SUBSCRIPTION_UID,"+getTimeStamp()+"\r\n");
		writer.write("PHONENUMBER,"+getPhoneNumber()+"\r\n");
		writer.write("KEYWORD,"+getKeyword()+"\r\n");
		writer.write("CONTENTS,"+getContents()+"\r\n");
		writer.close();
	}
	
	public String getTimeStamp() {
		return timeStamp;
	}
	public String getPushId() {
		return pushId;
	}
	public String getInboundId() {
		return inboundId;
	}
	public String getSubscriptionUid() {
		return subscriptionUid;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public String getContents() {
		return contents;
	}
	public String getKeyword() {
		return keyword;
	}
	public String getDataCapture() {
		return dataCapture;
	}
	public String getAttachment() {
		return attachment;
	}
	public String getDatasetId() {
		return datasetId;
	}
	public String getDatasetName() {
		return datasetName;
	}

	public void parsing(String inboundMsg) throws Exception{
		timeStamp = getValueFromExpression(inboundMsg, "/TRUMPIA/TIME_STAMP");
		pushId = getValueFromExpression(inboundMsg, "/TRUMPIA/PUSH_ID");
		inboundId = getValueFromExpression(inboundMsg, "/TRUMPIA/INBOUND_ID");
		subscriptionUid = getValueFromExpression(inboundMsg, "/TRUMPIA/SUBSCRIPTION_UID");
		phoneNumber = getValueFromExpression(inboundMsg, "/TRUMPIA/PHONENUMBER");
		keyword = getValueFromExpression(inboundMsg, "/TRUMPIA/KEYWORD");
		dataCapture = getValueFromExpression(inboundMsg, "/TRUMPIA/DATACAPTURE");
		contents = getValueFromExpression(inboundMsg, "/TRUMPIA/contents");
		attachment = getValueFromExpression(inboundMsg, "/TRUMPIA/ATTACHMENT");
		datasetId = getValueFromExpression(inboundMsg, "/TRUMPIA/DATASET_ID");
	}
	
	public String getValueFromExpression(String xml, String expression) throws Exception{
		DocumentBuilder builder = builderFactory.newDocumentBuilder();

		Document xmlDocument = builder.parse(new ByteArrayInputStream(xml.getBytes()));
		
		XPath xPath =  XPathFactory.newInstance().newXPath();
		
		String output = xPath.compile(expression).evaluate(xmlDocument);
		return output;
	}
}
