
public class UrlFactory implements EventFactory{

	@Override
	public Object newInstance(int i) {
		return new Url("url:"+i);
	}

}
