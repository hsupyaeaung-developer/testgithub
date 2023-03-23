package model.com.message;

public class Messaage {
private String content;
private String type;

public Messaage(String content, String type) {
	super();
	this.content = content;
	this.type = type;
}
public String getContent() {
	return content;
}
public void setContent(String content) {
	this.content = content;
}
public String getType() {
	return type;
}
public void setType(String type) {
	this.type = type;
}

}
