import java.util.Optional;

public class StringUtil {
	public static String removeLastCharacter(String str) {
		String result = Optional.ofNullable(str)
		.filter(sStr -> sStr.length() != 0)
		.map(sStr -> sStr.substring(0, sStr.length() - 1))
		.orElse(str);
		return result;
	 }
}
