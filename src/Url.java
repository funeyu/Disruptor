
public class Url {
	private String url;
	public Url(String url){
		this.url=url;
	}
	public String getString(){
		return this.url;
	}
	public void print(int i){
		System.out.println("url:i======="+i+this.url);
	}
}
