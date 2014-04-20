package archived.security_lab.mutualAuth;

import java.math.BigInteger;

/**
 * Created by avery_000 on 10-Apr-14.
 */
public class TestClient6 {
	public static void main(String[] args) throws Exception {
		int i = 0;
		try {
			String s = args[1];
			BigInteger ii = new BigInteger(s.substring(1));
			i = ii.intValue();
			System.out.println(i);
		} catch (Exception e) {
			System.err.printf("Usage: %s -c T[2-5]%n", TestClient6.class.getCanonicalName());
			System.exit(1);
		}

		switch (i) {
			case 2:
				TestClient2.main(new String[0]);
				System.exit(0);
			case 3:
				TestClient3.main(new String[0]);
				System.exit(0);
			case 4:
				TestClient4.main(new String[0]);
				System.exit(0);
			case 5:
				TestClient5.main(new String[0]);
				System.exit(0);
			default:
				System.err.printf("*** Usage: %s -c T[2-6]%n", TestClient6.class.getCanonicalName());
				System.exit(1);
		}
	}
}
