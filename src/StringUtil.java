import java.util.Optional;

public class StringUtil {
	public static String removeLastCharacter(String str) {
		String result = Optional.ofNullable(str)
		.filter(sStr -> sStr.length() != 0)
		.map(sStr -> sStr.substring(0, sStr.length() - 1))
		.orElse(str);
		return result;
	 }

	 public static boolean compare(String str1, String str2) {
		return (str1 == null ? str2 == null : str1.equals(str2));
	 }
}
