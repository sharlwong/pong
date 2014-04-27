package multipong;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class RSATools {
	private static final boolean EXCEPTIONS_ON = true;
	private static final boolean PING_ON       = true;
	private static final boolean PRINTING_ON   = true;
	private static final String  SPACER        = "; ";
	private static final boolean WARNINGS_ON   = true;

	public static String base64(byte[] bytes) {
		BASE64Encoder base64 = new BASE64Encoder();
		return base64.encode(bytes);
	}

	public static byte[] base64(String string) {
		try {
			return new BASE64Decoder().decodeBuffer(string.replaceAll("[^a-zA-Z0-9+/=]", ""));
		} catch (Exception ignored) { }
		return new byte[0];
	}

	public static <A> A[] clone(A[] z) {
		return z.clone();
	}

	public static <T> List<T> clone(Iterable<T> things) {
		List<T> out = new ArrayList<T>();
		for (T thing : things) out.add(thing);
		return out;
	}

	public static List<Integer> coPrimes(int n) {
		List<Integer> l = new ArrayList<Integer>();
		for (int i = 1; i < n; i++) if (gcd(n, i) == 1) l.add(i);
		return l;
	}

	public static <A extends Number, B extends Number> int compare(A o1, B o2) {
		return createBigDecimal(o1).compareTo(createBigDecimal(o2));
	}

	private static <T extends Number> BigDecimal createBigDecimal(T value) {
		BigDecimal result;
		if (value instanceof Short) result = BigDecimal.valueOf(value.shortValue());
		else if (value instanceof Long) result = BigDecimal.valueOf(value.longValue());
		else if (value instanceof Float) result = BigDecimal.valueOf(value.floatValue());
		else if (value instanceof Double) result = BigDecimal.valueOf(value.doubleValue());
		else if (value instanceof Integer) result = BigDecimal.valueOf(value.intValue());
		else throw new IllegalArgumentException("unsupported Number subtype: " + value.getClass().getName());
		if ((result == null)) throw new AssertionError();
		return result;
	}

	public static byte[] decrypt(String instance, Key key, byte[] encrypted) {
		try {
			Cipher cipher = Cipher.getInstance(instance);
			cipher.init(Cipher.DECRYPT_MODE, key);
			return cipher.doFinal(encrypted);
		} catch (Exception ignored) { }
		return new byte[0];
	}

	public static Object deserialize(byte[] yourBytes) {
		ByteArrayInputStream bis = new ByteArrayInputStream(yourBytes);
		ObjectInput in = null;
		Object o = null;
		try {
			in = new ObjectInputStream(bis);
			o = in.readObject();
		} catch (IOException ignored) {
		} catch (ClassNotFoundException ignored) {
		} finally {
			try {
				bis.close();
			} catch (IOException ignored) { }
			try {
				if (in != null) in.close();
			} catch (Exception ignored) { }
		}
		return o;
	}

	public static String digest(String message) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA");
		} catch (Exception ignored) { }
		assert md != null;
		md.update(message.getBytes());
		byte[] digest = md.digest();
		return base64(digest);
	}

	public static byte[] encrypt(String instance, Key key, byte[] plainText) {
		try {
			Cipher cipher = Cipher.getInstance(instance);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return cipher.doFinal(plainText);
		} catch (Exception ignored) { }
		return new byte[0];
	}

	public static void explode() {
		if (EXCEPTIONS_ON) throw new RSATools.Bakuhatsu("\nERROR!\n(UNSPECIFIED)");
	}

	public static void explode(String description) {
		if (EXCEPTIONS_ON) throw new Bakuhatsu("\nERROR!\nReason:  " + description);
	}

	/**
	 * pollard-rho
	 */
	public static List<BigInteger> factorize(BigInteger input) {
		List<BigInteger> out = new LinkedList<BigInteger>();
		if (input.compareTo(BigInteger.ONE) <= 0) return out;
		if (input.isProbablePrime(20)) return out.add(input) ? out : null;

		// pollard-rho algorithm to find one divisor
		SecureRandom random = new SecureRandom();
		BigInteger divisor;
		BigInteger c = new BigInteger(input.bitLength(), random);
		BigInteger x = new BigInteger(input.bitLength(), random);
		BigInteger xx = x;
		do {
			x = x.multiply(x).mod(input).add(c).mod(input);
			xx = xx.multiply(xx).mod(input).add(c).mod(input);
			xx = xx.multiply(xx).mod(input).add(c).mod(input);
			divisor = x.subtract(xx).gcd(input);
		} while (divisor.compareTo(BigInteger.ONE) == 0);

		out.addAll(factorize(divisor));
		out.addAll(factorize(input.divide(divisor)));
		return out;
	}

	/**
	 * pollard-brent-rho
	 */
	public static BigInteger factorize2(BigInteger n) {
		BigInteger TWO = BigInteger.valueOf(2);
		BigInteger THREE = BigInteger.valueOf(3);
		BigInteger TEN_THOUSAND = BigInteger.valueOf(10000);
		BigInteger k;
		BigInteger r;
		BigInteger i;
		BigInteger m;
		BigInteger iter;
		BigInteger z;
		BigInteger x;
		BigInteger y;
		BigInteger q;
		BigInteger ys;

		m = BigInteger.TEN;
		r = BigInteger.ONE;
		iter = BigInteger.ZERO;
		y = BigInteger.ZERO;
		q = BigInteger.ONE;

		do {
			x = y;
			for (i = BigInteger.ONE; i.compareTo(r) <= 0; i = i.add(BigInteger.ONE))
				y = ((y.multiply(y)).add(THREE)).mod(n);
			k = BigInteger.ZERO;
			do {
				iter = iter.add(BigInteger.ONE);
				ys = y;
				for (i = BigInteger.ONE; i.compareTo(min(m, r.subtract(k))) <= 0; i = i.add(BigInteger.ONE)) {
					y = ((y.multiply(y)).add(THREE)).mod(n);
					q = ((y.subtract(x)).multiply(q)).mod(n);
				}
				z = n.gcd(q);
				k = k.add(m);
			} while (k.compareTo(r) < 0 && z.compareTo(BigInteger.ONE) == 0);
			r = r.multiply(TWO);
		} while (z.compareTo(BigInteger.ONE) == 0 && iter.compareTo(TEN_THOUSAND) < 0);

		if (z.compareTo(n) == 0) do {
			ys = ((ys.multiply(ys)).add(THREE)).mod(n);
			z = n.gcd(ys.subtract(x));
		} while (z.compareTo(BigInteger.ONE) == 0);

		return z;
	}

	public static int gcd(int p, int q) {
		if (q == 0) return p;
		return gcd(q, p % q);
	}

	public static String getCWD() {
		return System.getProperty("user.dir");
	}

	public static File getFile(String fileName) {
		if (osCheck().equals("win")) return new File(getCWD() + "\\" + fileName);
		if (osCheck().equals("mac")) return new File(fileName);
		explode("unrecognized os!");
		return null;
	}

	public static String getIP() {
		try {
			for (final Enumeration<NetworkInterface> interfaces2 = NetworkInterface.getNetworkInterfaces(); interfaces2.hasMoreElements(); ) {
				final NetworkInterface cur = interfaces2.nextElement();
				if (cur.isLoopback()) continue;
				for (final InterfaceAddress address : cur.getInterfaceAddresses()) {
					final InetAddress internet_address = address.getAddress();
					if (!(internet_address instanceof Inet4Address)) continue;
					if (address.getNetworkPrefixLength() == -1) return internet_address.getHostAddress();
				}
			}
		} catch (SocketException ignored) {}
		return null;
	}

	public static String getIPv6() {
		try {
			for (final Enumeration<NetworkInterface> interfaces2 = NetworkInterface.getNetworkInterfaces(); interfaces2.hasMoreElements(); ) {
				final NetworkInterface cur = interfaces2.nextElement();
				if (cur.isLoopback()) continue;
				for (final InterfaceAddress address : cur.getInterfaceAddresses()) {
					final InetAddress internet_address = address.getAddress();
					if (!(internet_address instanceof Inet6Address)) continue;
					if (address.getNetworkPrefixLength() == -1) return internet_address.getHostAddress();
				}
			}
		} catch (SocketException ignored) {}
		return null;
	}

	public static Object getUnencryptedObject(Socket socket) {
		Object obj = null;
		try {
			ObjectInputStream obIn = new ObjectInputStream(socket.getInputStream());
			obj = obIn.readObject();
		} catch (Exception ignored) { }
		return obj;
	}

	public static boolean isPrime(Number num) {
		BigInteger temp = BigInteger.valueOf(num.longValue());
		return temp.isProbablePrime(30);
	}

	public static List<Double> linspace(double first, double last, double step) {
		List<Double> l = new ArrayList<Double>();
		double dist = last - first;
		int steps = (int) (dist / step);
		if (steps <= 0) return l;
		for (double i = 0; i <= steps; i++) {
			l.add(first + i * step);
		}
		return l;
	}

	public static <T extends Number> List<Double> makeDoubleList(Iterable<T> l) {
		List<Double> out = new ArrayList<Double>();
		for (T aL : l) out.add(aL.doubleValue());
		return out;
	}

	public static <T extends Number> List<Double> makeDoubleList(T[] l) {
		List<Double> out = new ArrayList<Double>();
		for (Number i : l) {
			out.add(i.doubleValue());
		}
		return out;
	}

	public static <T extends Number> List<Double> makeDoubleList(T l) {
		return new ArrayList<Double>(Arrays.asList(l.doubleValue()));
	}

	public static <T extends Comparable<T>> T max(T a, T b) {
		return (a.compareTo(b) > 0) ? a : b;
	}

	public static <T extends Comparable<T>> T max(Collection<T> things) {
		return Collections.max(things);
	}

	public static <T extends Number> double mean(Collection<T> l) {
		return sum(l) / l.size();
	}

	public static <T extends Number> double median(Iterable<T> z) {
		List<Double> l = sorted(makeDoubleList(z));
		int k = l.size();
		if (k % 2 == 0) return (l.get(k / 2) + l.get(k / 2 + 1)) / 2;
		return l.get(k / 2 + 1);
	}

	public static <T extends Comparable<T>> T min(T a, T b) {
		return (a.compareTo(b) < 0) ? a : b;
	}

	public static <T extends Comparable<T>> T min(Collection<T> things) {
		return Collections.min(things);
	}

	public static String nonce(int length) {
		Random random = new SecureRandom();
		byte[] bytes = new byte[length * 2];
		random.nextBytes(bytes);
		String temp = base64(bytes);
		if (temp.length() > length) temp = temp.substring(0, length);
		return temp;
	}

	public static String nonce() {
		return nonce(128);
	}

	/**
	 * A simple function to find the system OS
	 *
	 * @return a short string naming the OS
	 */
	public static String osCheck() {
		if (System.getProperty("os.name").toLowerCase().contains("win")) return "win";
		if (System.getProperty("os.name").toLowerCase().contains("mac")) return "mac";
		if (System.getProperty("os.name").toLowerCase().contains("nux")) return "unix";
		if (System.getProperty("os.name").toLowerCase().contains("nix")) return "unix";
		if (System.getProperty("os.name").toLowerCase().contains("aix")) return "unix";
		if (System.getProperty("os.name").toLowerCase().contains("sunos")) return "solaris";
		return "unknown";
	}

	public static void ping(String message) {
		if (PING_ON) System.out.println("PING! -> " + message);
	}

	public static void ping() {
		if (PING_ON) System.out.println("PING!");
	}

	public static void playMidi(String fileName) {
		try {
			// Create a sequencer for the sequence
			Sequencer sequencer = MidiSystem.getSequencer();

			/* Make from file */
			//Sequence sequence1 = MidiSystem.getSequence(getFile(fileName));

			/*
			Or create a stream from a file
			Sets the current sequence on which the sequencer operates.
			The stream must point to MIDI file data.
			*/
			// InputStream sequence2 = new BufferedInputStream(new FileInputStream(getFile(fileName)));

			/* Make from URL */
			Sequence sequence3 = MidiSystem.getSequence(new URL("http://josh.agarrado.net/archived.music/anime/get/4060/angel-beats-my-soul-your-beats-3.mid"));

			/* order doesn't matter for these two following commands */
			sequencer.setSequence(sequence3);
			sequencer.open();

			/* Starts playback of the MIDI data in the currently loaded sequence. */
			sequencer.start();

			/*
			Wait for song to finish
			java will not respond until it's done
			*/
			while (sequencer.isRunning()) Thread.yield();
			sequencer.close();
		} catch (Exception e) {
			System.out.print("FAIL: " + e.toString());
		}
	}

	public static void print(StringBuffer sb) {
		print(str(sb));
	}

	public static void print(String string) {
		if (PRINTING_ON) System.out.println(string);
	}

	public static void print(Collection<?> l1, Collection<?> l2) {
		print(str(l1, l2));
	}

	public static void print(Collection<?> z) {
		print(str(z));
	}

	public static void print(StringBuilder sb) {
		print(str(sb));
	}

	public static void print(Map<?, ?> map) {
		print(str(map));
	}

	public static <T> void print(T[] z) {
		print(str(z));
	}

	public static void print(byte[] z) {
		print(str(z));
	}

	public static void print(Object z) {
		print(z.toString());
	}

	public static List<Double> range(double length) {
		List<Double> l = new ArrayList<Double>();
		for (double i = 0; i < length; i++) l.add(i);
		return l;
	}

	public static List<Double> range(double start, double length) {
		List<Double> l = new ArrayList<Double>();
		for (double i = start; i < length; i++) l.add(i);
		return l;
	}

	public static List<Double> range(double start, double length, double step) {
		List<Double> l = new ArrayList<Double>();
		if (step <= 0) return l;
		for (double i = start; i < length; i += step) l.add(i);
		return l;
	}

	public static String read(String fileName) {
		return read(getFile(fileName));
	}

	public static String read(File file) {
		if (file == null) explode("File must not be null.");
		assert file != null;
		if (!file.exists()) explode("File does not exist: " + file);
		if (!file.isFile()) explode("Must not be a directory: " + file);
		if (!file.canRead()) explode("File cannot be read: " + file);

		StringBuilder outString = new StringBuilder();
		String temp;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			while ((temp = reader.readLine()) != null) outString.append(temp).append("\n");
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			explode("\nFILE READING ERROR: " + e);
		}
		return outString.toString();
	}

	public static String read(String directoryPath, String fileName) {
		return read(new File(directoryPath, fileName));
	}

	public static List<String> readLines(String fileName) {
		return readLines(getFile(fileName));
	}

	public static List<String> readLines(File file) {
		List<String> lines = new LinkedList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			for (String line; (line = br.readLine()) != null; ) {
				lines.add(line);
			}
			// line is not visible here.
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lines;
	}

	public static int regexCount(String regex, CharSequence string) {
		int count = 0;
		Matcher m = Pattern.compile(regex).matcher(string);
		while (m.find()) count++;
		return count;
	}

	public static int regexFirstIndex(String regex, CharSequence string) {
		Matcher m = Pattern.compile(regex).matcher(string);
		return m.find() ? m.start() : -1;
	}

	public static String regexFirstMatch(String regex, CharSequence string) {
		Matcher m = Pattern.compile(regex).matcher(string);
		return m.find() ? m.group() : null;
	}

	public static boolean regexIsMatch(String regex, CharSequence string) {
		return Pattern.matches(regex, string);
	}

	public static List<String> regexMatches(String regex, CharSequence string) {
		List<String> matchList = new ArrayList<String>();
		Matcher m = Pattern.compile(regex).matcher(string);
		while (m.find()) matchList.add(m.group());
		return matchList;
	}

	private static boolean regexPrimeCheck(int n) {
		if (n >= 0) return false;
		// will likely crash computer
		return !new String(new char[n]).matches(".?|(..+?)\\1+");
	}

	public static <E> List<E> reverse(Iterable<E> z) {
		LinkedList<E> l = new LinkedList<E>();
		for (E elem : z) l.addFirst(elem);
		return l;
	}

	public static void sendUnencryptedObject(Socket socket, Object message) {
		try {
			ObjectOutputStream obOut = new ObjectOutputStream(socket.getOutputStream());
			obOut.writeObject(message);
			obOut.flush();
		} catch (Exception ignored) { }
	}

	public static byte[] serialize(Object yourObject) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		try {
			out = new ObjectOutputStream(bos);
			out.writeObject(yourObject);
		} catch (Exception ignored) {
		} finally {
			try {
				if (out != null) out.close();
			} catch (Exception ignored) { }
			try {
				bos.close();
			} catch (Exception ignored) { }
		}
		return bos.toByteArray();
	}

	public static <T extends Comparable<T>> List<T> sorted(Iterable<T> things) {
		List<T> out = clone(things);
		Collections.sort(out);
		return out;
	}

	public static <T extends Comparable<T>> T[] sorted(T[] k) {
		List<T> temp = sorted(Arrays.asList(k));
		T[] z = k.clone();
		for (int i = 0; i < z.length; i++) z[i] = temp.get(i);
		return z;
	}

	public static String sorted(String z) {
		return new String(sorted(z.toCharArray()));
	}

	public static char[] sorted(char[] k) {
		char[] z = k.clone();
		boolean unsorted;
		do {
			unsorted = false;
			for (int i = 1; i < z.length; i++) {
				if (z[i] < z[i - 1]) {
					char a = z[i];
					z[i] = z[i - 1];
					z[i - 1] = a;
					unsorted = true;
				}
			}
		} while (unsorted);
		return z;
	}

	public static String str(Iterable<?> col, String delimiter) {
		StringBuilder sb = new StringBuilder();
		Iterator<?> iterator = col.iterator();
		if (iterator.hasNext()) sb.append(iterator.next().toString());
		while (iterator.hasNext()) {
			sb.append(delimiter);
			sb.append(iterator.next().toString());
		}
		return sb.toString();
	}

	public static <A> String str(A thing) {
		return thing.toString();
	}

	public static String str(Collection<?> l1, Collection<?> l2) {
		return str(l1, l2, true, ":");
	}

	public static String str(Iterable<?> l1, Iterable<?> l2, boolean alignLeft, String separator) {
		//make lists of strings
		List<String> relist1 = new LinkedList<String>();
		List<String> relist2 = new LinkedList<String>();
		for (Object o : l1) relist1.add(o.toString());
		for (Object o : l2) relist2.add(o.toString());

		//find longest string in first list
		StringBuilder stringBuilder = new StringBuilder();
		int len1 = 0;
		for (String aRelist1 : relist1) if (aRelist1.length() > len1) len1 = aRelist1.length();

		//make lines
		int maxLen = relist1.size() > relist2.size() ? relist1.size() : relist2.size();
		String temp;
		for (int i = 0; i < maxLen; i++) {
			temp = i < relist1.size() ? relist1.get(i) : "";
			stringBuilder.append(String.format("%" + (alignLeft ? "-" : "") + len1 + "s " + separator + " " + (i < relist2.size() ? relist2.get(i) : "") + "\n", temp));
		}
		return stringBuilder.toString();
	}

	public static String str(Collection<?> z) {
		return str(z, SPACER);
	}

	public static String str(Map<?, ?> map) {
		List<String> keys = new ArrayList<String>();
		List<String> entries = new ArrayList<String>();
		for (Map.Entry<?, ?> e : map.entrySet()) {
			keys.add(e.getKey().toString());
			entries.add(e.getValue().toString());
		}
		return str(keys, entries, true, "->");
	}

	public static String str(byte[] bytes) {
		return new String(bytes, Charset.defaultCharset());
	}

	public static <T> String str(T[] z) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < z.length; i++) {
			stringBuilder.append(z[i]);
			if (i < (z.length - 1)) stringBuilder.append(SPACER);
		}
		return stringBuilder.toString();
	}

	public static <T extends Number> double sum(Collection<T> l) {
		double result = 0;
		for (T aL : l) result += aL.doubleValue();
		return result;
	}

	public static void warning(String description) {
		if (WARNINGS_ON) throw new RSATools.Bakuhatsu("\nWARNING!\nReason:  " + description);
	}

	public static void warning() {
		if (WARNINGS_ON) throw new RSATools.Bakuhatsu("\nWARNING!\n(UNSPECIFIED)");
	}

	static public void writeFile(File aFile, Collection<String> lines) {
		writeFile(aFile, str(lines, "\n"));
	}

	static public void writeFile(File aFile, String contents) {
		if (aFile == null) explode("File must not be null.");
		if (contents == null) explode("Contents must not be null.");
		assert aFile != null;
		if (!aFile.exists()) explode("File does not exist: " + aFile);
		if (!aFile.isFile()) explode("Must not be a directory: " + aFile);
		if (!aFile.canWrite()) explode("File cannot be written: " + aFile);

		try {
			Writer output = new BufferedWriter(new FileWriter(aFile));
			assert contents != null;
			output.write(contents);
			/* flush and close both "output" and its underlying FileWriter */
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
			explode("Failed to write to file: " + aFile);
		}
	}

	static public void writeFile(String fileName, Collection<String> lines) {
		writeFile(getFile(fileName), str(lines, "\n"));
	}

	static public void writeFile(String fileName, String contents) {
		writeFile(getFile(fileName), contents);
	}

	public static class AESHelper {
		SecretKeySpec keySpec;

		public AESHelper(String keyString) {
			byte[] key = base64(digest(keyString));
			key = Arrays.copyOf(key, 16);
			keySpec = new SecretKeySpec(key, "AES");
		}

		public Object decryptObject(String encrypted) {
			byte[] decrypted = decrypt("AES/ECB/PKCS5Padding", keySpec, base64(encrypted));
			return deserialize(decrypted);
		}

		public String decryptString(String encrypted) {
			return new String(decrypt("AES/ECB/PKCS5Padding", keySpec, base64(encrypted)));
		}

		public String encryptObject(Object message) {
			byte[] bytes = serialize(message);
			return base64(encrypt("AES/ECB/PKCS5Padding", keySpec, bytes));
		}

		public String encryptString(String message) {
			return base64(encrypt("AES/ECB/PKCS5Padding", keySpec, message.getBytes()));
		}

		public String getMessage(Socket socket) {
			Object obj = getUnencryptedObject(socket);
			return decryptString((String) obj);
		}

		public Object getObject(Socket socket) {
			Object obj = getUnencryptedObject(socket);
			return decryptObject((String) obj);
		}

		public void sendMessage(Socket socket, String message) {
			sendUnencryptedObject(socket, encryptString(message));
		}

		public void sendObject(Socket socket, Object object) {
			sendUnencryptedObject(socket, encryptObject(object));
		}
	}

	static class BASE64Decoder extends sun.misc.CharacterDecoder {

		private static final char pem_array[] = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};
		private static final byte pem_convert_array[];
		byte decode_buffer[];

		static {
			pem_convert_array = new byte[256];
			for (int i = 0; i < 255; i++)
				pem_convert_array[i] = -1;

			for (int j = 0; j < pem_array.length; j++)
				pem_convert_array[pem_array[j]] = (byte) j;
		}

		public BASE64Decoder() {
			decode_buffer = new byte[4];
		}

		protected int bytesPerAtom() {
			return 4;
		}

		protected int bytesPerLine() {
			return 72;
		}

		protected void decodeAtom(PushbackInputStream pushbackinputstream, OutputStream outputstream, int i) throws IOException {
			byte byte0 = -1;
			byte byte1 = -1;
			byte byte2 = -1;
			byte byte3 = -1;
			if (i < 2) throw new CEFormatException("RSATools.BASE64Decoder: Not enough bytes for an atom.");
			int j;
			do {
				j = pushbackinputstream.read();
				if (j == -1) throw new sun.misc.CEStreamExhausted();
			} while (j == 10 || j == 13);
			decode_buffer[0] = (byte) j;
			j = readFully(pushbackinputstream, decode_buffer, 1, i - 1);
			if (j == -1) throw new CEStreamExhausted();
			if (i > 3 && decode_buffer[3] == 61) i = 3;
			if (i > 2 && decode_buffer[2] == 61) i = 2;
			switch (i) {
				case 4: // '\004'
					byte3 = pem_convert_array[decode_buffer[3] & 255];
					// fall through

				case 3: // '\003'
					byte2 = pem_convert_array[decode_buffer[2] & 255];
					// fall through

				case 2: // '\002'
					byte1 = pem_convert_array[decode_buffer[1] & 255];
					byte0 = pem_convert_array[decode_buffer[0] & 255];
					// fall through

				default:
					switch (i) {
						case 2: // '\002'
							outputstream.write((byte) (byte0 << 2 & 252 | byte1 >>> 4 & 3));
							break;

						case 3: // '\003'
							outputstream.write((byte) (byte0 << 2 & 252 | byte1 >>> 4 & 3));
							outputstream.write((byte) (byte1 << 4 & 240 | byte2 >>> 2 & 15));
							break;

						case 4: // '\004'
							outputstream.write((byte) (byte0 << 2 & 252 | byte1 >>> 4 & 3));
							outputstream.write((byte) (byte1 << 4 & 240 | byte2 >>> 2 & 15));
							outputstream.write((byte) (byte2 << 6 & 192 | byte3 & 63));
							break;
					}
					break;
			}
		}
	}

	static class BASE64Encoder extends CharacterEncoder {

		private static final char pem_array[] = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};

		public BASE64Encoder() {}

		protected int bytesPerAtom() {
			return 3;
		}

		protected int bytesPerLine() {
			return 57;
		}

		protected void encodeAtom(OutputStream outputstream, byte abyte0[], int i, int j) throws IOException {
			if (j == 1) {
				byte byte0 = abyte0[i];
				int k = 0;
				boolean flag = false;
				outputstream.write(pem_array[byte0 >>> 2 & 63]);
				outputstream.write(pem_array[(byte0 << 4 & 48) + (k >>> 4 & 15)]);
				outputstream.write(61);
				outputstream.write(61);
			}
			else if (j == 2) {
				byte byte1 = abyte0[i];
				byte byte3 = abyte0[i + 1];
				int l = 0;
				outputstream.write(pem_array[byte1 >>> 2 & 63]);
				outputstream.write(pem_array[(byte1 << 4 & 48) + (byte3 >>> 4 & 15)]);
				outputstream.write(pem_array[(byte3 << 2 & 60) + (l >>> 6 & 3)]);
				outputstream.write(61);
			}
			else {
				byte byte2 = abyte0[i];
				byte byte4 = abyte0[i + 1];
				byte byte5 = abyte0[i + 2];
				outputstream.write(pem_array[byte2 >>> 2 & 63]);
				outputstream.write(pem_array[(byte2 << 4 & 48) + (byte4 >>> 4 & 15)]);
				outputstream.write(pem_array[(byte4 << 2 & 60) + (byte5 >>> 6 & 3)]);
				outputstream.write(pem_array[byte5 & 63]);
			}
		}
	}

	/**
	 * a convenient exception class
	 * inherits runtime-exception
	 * so no declarations are necessary
	 */
	private static class Bakuhatsu extends RuntimeException {
		/**
		 * convenient way to throw runtime exceptions
		 *
		 * @param message will be shown when your program dies
		 */
		private Bakuhatsu(String message) {
			super(message);
		}

		private Bakuhatsu() {
			super("Exception Thrown!");
		}
	}

	static class CEFormatException extends IOException {

		public CEFormatException(String s) {
			super(s);
		}
	}

	static class CEStreamExhausted extends IOException {

		public CEStreamExhausted() {
		}
	}

	abstract static class CharacterDecoder {

		public CharacterDecoder() { }

		protected abstract int bytesPerAtom();

		protected abstract int bytesPerLine();

		protected void decodeAtom(PushbackInputStream pushbackinputstream, OutputStream outputstream, int i) throws IOException {
			throw new sun.misc.CEStreamExhausted();
		}

		public void decodeBuffer(InputStream inputstream, OutputStream outputstream) throws IOException {
			int j = 0;
			PushbackInputStream pushbackinputstream = new PushbackInputStream(inputstream);
			decodeBufferPrefix(pushbackinputstream, outputstream);
			try {
				do {
					int k = decodeLinePrefix(pushbackinputstream, outputstream);
					int i;
					for (i = 0; i + bytesPerAtom() < k; i += bytesPerAtom()) {
						decodeAtom(pushbackinputstream, outputstream, bytesPerAtom());
						j += bytesPerAtom();
					}

					if (i + bytesPerAtom() == k) {
						decodeAtom(pushbackinputstream, outputstream, bytesPerAtom());
						j += bytesPerAtom();
					}
					else {
						decodeAtom(pushbackinputstream, outputstream, k - i);
						j += k - i;
					}
					decodeLineSuffix(pushbackinputstream, outputstream);
				} while (true);
			} catch (sun.misc.CEStreamExhausted cestreamexhausted) {
				decodeBufferSuffix(pushbackinputstream, outputstream);
			}
		}

		public byte[] decodeBuffer(String s) throws IOException {
			byte abyte0[] = new byte[s.length()];
			s.getBytes(0, s.length(), abyte0, 0);
			ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(abyte0);
			ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
			decodeBuffer(((InputStream) (bytearrayinputstream)), ((OutputStream) (bytearrayoutputstream)));
			return bytearrayoutputstream.toByteArray();
		}

		public byte[] decodeBuffer(InputStream inputstream) throws IOException {
			ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
			decodeBuffer(inputstream, ((OutputStream) (bytearrayoutputstream)));
			return bytearrayoutputstream.toByteArray();
		}

		protected void decodeBufferPrefix(PushbackInputStream pushbackinputstream, OutputStream outputstream) throws IOException {
		}

		protected void decodeBufferSuffix(PushbackInputStream pushbackinputstream, OutputStream outputstream) throws IOException {
		}

		public ByteBuffer decodeBufferToByteBuffer(String s) throws IOException {
			return ByteBuffer.wrap(decodeBuffer(s));
		}

		public ByteBuffer decodeBufferToByteBuffer(InputStream inputstream) throws IOException {
			return ByteBuffer.wrap(decodeBuffer(inputstream));
		}

		protected int decodeLinePrefix(PushbackInputStream pushbackinputstream, OutputStream outputstream) throws IOException {
			return bytesPerLine();
		}

		protected void decodeLineSuffix(PushbackInputStream pushbackinputstream, OutputStream outputstream) throws IOException {
		}

		protected int readFully(InputStream inputstream, byte abyte0[], int i, int j) throws IOException {
			for (int k = 0; k < j; k++) {
				int l = inputstream.read();
				if (l == -1) return k != 0 ? k : -1;
				abyte0[k + i] = (byte) l;
			}

			return j;
		}
	}

	abstract static class CharacterEncoder {

		protected PrintStream pStream;

		public CharacterEncoder() {
		}

		protected abstract int bytesPerAtom();

		protected abstract int bytesPerLine();

		public void encode(InputStream inputstream, OutputStream outputstream) throws IOException {
			byte abyte0[] = new byte[bytesPerLine()];
			encodeBufferPrefix(outputstream);
			do {
				int j = readFully(inputstream, abyte0);
				if (j == 0) break;
				encodeLinePrefix(outputstream, j);
				for (int i = 0; i < j; i += bytesPerAtom())
					if (i + bytesPerAtom() <= j) encodeAtom(outputstream, abyte0, i, bytesPerAtom());
					else encodeAtom(outputstream, abyte0, i, j - i);

				if (j < bytesPerLine()) break;
				encodeLineSuffix(outputstream);
			} while (true);
			encodeBufferSuffix(outputstream);
		}

		public void encode(byte abyte0[], OutputStream outputstream) throws IOException {
			ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(abyte0);
			encode(((InputStream) (bytearrayinputstream)), outputstream);
		}

		public String encode(byte abyte0[]) {
			ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
			ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(abyte0);
			String s = null;
			try {
				encode(((InputStream) (bytearrayinputstream)), ((OutputStream) (bytearrayoutputstream)));
				s = bytearrayoutputstream.toString("8859_1");
			} catch (Exception exception) {
				throw new Error("RSATools.CharacterEncoder.encode internal error");
			}
			return s;
		}

		public void encode(ByteBuffer bytebuffer, OutputStream outputstream) throws IOException {
			byte abyte0[] = getBytes(bytebuffer);
			encode(abyte0, outputstream);
		}

		public String encode(ByteBuffer bytebuffer) {
			byte abyte0[] = getBytes(bytebuffer);
			return encode(abyte0);
		}

		protected abstract void encodeAtom(OutputStream outputstream, byte abyte0[], int i, int j) throws IOException;

		public void encodeBuffer(InputStream inputstream, OutputStream outputstream) throws IOException {
			byte abyte0[] = new byte[bytesPerLine()];
			encodeBufferPrefix(outputstream);
			int j;
			do {
				j = readFully(inputstream, abyte0);
				if (j == 0) break;
				encodeLinePrefix(outputstream, j);
				for (int i = 0; i < j; i += bytesPerAtom())
					if (i + bytesPerAtom() <= j) encodeAtom(outputstream, abyte0, i, bytesPerAtom());
					else encodeAtom(outputstream, abyte0, i, j - i);

				encodeLineSuffix(outputstream);
			} while (j >= bytesPerLine());
			encodeBufferSuffix(outputstream);
		}

		public void encodeBuffer(byte abyte0[], OutputStream outputstream) throws IOException {
			ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(abyte0);
			encodeBuffer(((InputStream) (bytearrayinputstream)), outputstream);
		}

		public String encodeBuffer(byte abyte0[]) {
			ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
			ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(abyte0);
			try {
				encodeBuffer(((InputStream) (bytearrayinputstream)), ((OutputStream) (bytearrayoutputstream)));
			} catch (Exception exception) {
				throw new Error("RSATools.CharacterEncoder.encodeBuffer internal error");
			}
			return bytearrayoutputstream.toString();
		}

		public void encodeBuffer(ByteBuffer bytebuffer, OutputStream outputstream) throws IOException {
			byte abyte0[] = getBytes(bytebuffer);
			encodeBuffer(abyte0, outputstream);
		}

		public String encodeBuffer(ByteBuffer bytebuffer) {
			byte abyte0[] = getBytes(bytebuffer);
			return encodeBuffer(abyte0);
		}

		protected void encodeBufferPrefix(OutputStream outputstream) throws IOException {
			pStream = new PrintStream(outputstream);
		}

		protected void encodeBufferSuffix(OutputStream outputstream) throws IOException {
		}

		protected void encodeLinePrefix(OutputStream outputstream, int i) throws IOException {
		}

		protected void encodeLineSuffix(OutputStream outputstream) throws IOException {
			pStream.println();
		}

		private byte[] getBytes(ByteBuffer bytebuffer) {
			byte abyte0[] = null;
			if (bytebuffer.hasArray()) {
				byte abyte1[] = bytebuffer.array();
				if (abyte1.length == bytebuffer.capacity() && abyte1.length == bytebuffer.remaining()) {
					abyte0 = abyte1;
					bytebuffer.position(bytebuffer.limit());
				}
			}
			if (abyte0 == null) {
				abyte0 = new byte[bytebuffer.remaining()];
				bytebuffer.get(abyte0);
			}
			return abyte0;
		}

		protected int readFully(InputStream inputstream, byte abyte0[]) throws IOException {
			for (int i = 0; i < abyte0.length; i++) {
				int j = inputstream.read();
				if (j == -1) return i;
				abyte0[i] = (byte) j;
			}

			return abyte0.length;
		}
	}

	public static class DamerauLevenshteinAlgorithm {
		private final int deleteCost, insertCost, replaceCost, swapCost;

		/**
		 * Constructor.
		 *
		 * @param deleteCost  the cost of deleting a character.
		 * @param insertCost  the cost of inserting a character.
		 * @param replaceCost the cost of replacing a character.
		 * @param swapCost    the cost of swapping two adjacent characters.
		 */
		public DamerauLevenshteinAlgorithm(int deleteCost, int insertCost, int replaceCost, int swapCost) {
		/*
		 * Required to facilitate the premise to the algorithm that two swaps of
		 * the same character are never required for optimality.
		 */
			if (2 * swapCost < insertCost + deleteCost)
				throw new IllegalArgumentException("Unsupported cost assignment");
			this.deleteCost = deleteCost;
			this.insertCost = insertCost;
			this.replaceCost = replaceCost;
			this.swapCost = swapCost;
		}

		/**
		 * Compute the Damerau-Levenshtein distance between the specified source
		 * string and the specified target string.
		 */
		public int execute(String source, String target) {
			if (source.length() == 0) return target.length() * insertCost;
			if (target.length() == 0) return source.length() * deleteCost;
			int[][] table = new int[source.length()][target.length()];
			Map<Character, Integer> sourceIndexByCharacter = new HashMap<Character, Integer>();
			if (source.charAt(0) != target.charAt(0)) table[0][0] = Math.min(replaceCost, deleteCost + insertCost);
			sourceIndexByCharacter.put(source.charAt(0), 0);
			for (int i = 1; i < source.length(); i++) {
				int deleteDistance = table[i - 1][0] + deleteCost;
				int insertDistance = (i + 1) * deleteCost + insertCost;
				int matchDistance = i * deleteCost + (source.charAt(i) == target.charAt(0) ? 0 : replaceCost);
				table[i][0] = Math.min(Math.min(deleteDistance, insertDistance), matchDistance);
			}
			for (int j = 1; j < target.length(); j++) {
				int deleteDistance = table[0][j - 1] + insertCost;
				int insertDistance = (j + 1) * insertCost + deleteCost;
				int matchDistance = j * insertCost + (source.charAt(0) == target.charAt(j) ? 0 : replaceCost);
				table[0][j] = Math.min(Math.min(deleteDistance, insertDistance), matchDistance);
			}
			for (int i = 1; i < source.length(); i++) {
				int maxSourceLetterMatchIndex = source.charAt(i) == target.charAt(0) ? 0 : -1;
				for (int j = 1; j < target.length(); j++) {
					Integer candidateSwapIndex = sourceIndexByCharacter.get(target.charAt(j));
					int jSwap = maxSourceLetterMatchIndex;
					int deleteDistance = table[i - 1][j] + deleteCost;
					int insertDistance = table[i][j - 1] + insertCost;
					int matchDistance = table[i - 1][j - 1];
					if (source.charAt(i) != target.charAt(j)) matchDistance += replaceCost;
					else maxSourceLetterMatchIndex = j;
					int swapDistance;
					if (candidateSwapIndex != null && jSwap != -1) {
						int iSwap = candidateSwapIndex;
						int preSwapCost;
						if (iSwap == 0 && jSwap == 0) preSwapCost = 0;
						else preSwapCost = table[Math.max(0, iSwap - 1)][Math.max(0, jSwap - 1)];
						swapDistance = preSwapCost + (i - iSwap - 1) * deleteCost + (j - jSwap - 1) * insertCost + swapCost;
					}
					else swapDistance = Integer.MAX_VALUE;
					table[i][j] = Math.min(Math.min(Math.min(deleteDistance, insertDistance), matchDistance), swapDistance);
				}
				sourceIndexByCharacter.put(source.charAt(i), i);
			}
			return table[source.length() - 1][target.length() - 1];
		}
	}

	public static class RSAPrivate {
		private final int RSAKeySize = 2048;
		private KeyPair keyPair;

		public RSAPrivate() {
			KeyPairGenerator RSAKeyGen = null;
			try {
				RSAKeyGen = KeyPairGenerator.getInstance("RSA");
			} catch (Exception ignored) { }
			SecureRandom random = new SecureRandom();
			assert RSAKeyGen != null;
			RSAKeyGen.initialize(RSAKeySize, random);
			keyPair = RSAKeyGen.generateKeyPair();
		}

		public String decryptRSA(String encryptedBase64) {
			byte[] decode64 = base64(encryptedBase64);
			return new String(decrypt("RSA/ECB/PKCS1Padding", keyPair.getPrivate(), decode64));
		}

		public String getMessage(Socket socket) {
			Object obj = getUnencryptedObject(socket);
			return decryptRSA((String) obj);
		}

		protected PrivateKey getPrivateKey() {
			return keyPair.getPrivate();
		}

		public byte[] getPublicKeyEncoded() {
			return keyPair.getPublic().getEncoded();
		}

		public void sendKey(Socket socket) {
			byte[] encodedKey = keyPair.getPublic().getEncoded();
			ByteBuffer bb = ByteBuffer.allocate(4);
			bb.putInt(encodedKey.length);
			try {
				socket.getOutputStream().write(bb.array());
				socket.getOutputStream().write(encodedKey);
				socket.getOutputStream().flush();
			} catch (IOException e) { }
		}

		public void sendSignature(Socket socket, String message) {
			sendUnencryptedObject(socket, sign(message));
		}

		public String sign(String message) {
			try {
				/* Create a Signature object and initialize it with the private key */
				Signature rsaSignature = Signature.getInstance("SHA1withRSA");
				rsaSignature.initSign(keyPair.getPrivate());

	            /* Update and sign the data */
				byte[] buffer = message.getBytes();
				rsaSignature.update(buffer, 0, buffer.length);

	            /* Now that the data to be signed has been processed, generate a signature for it */
				byte[] generatedSignature = rsaSignature.sign();

				/* done now return */
				return base64(generatedSignature);
			} catch (Exception ignored) { }
			return "";
		}
	}

	public static class RSAPublic {
		private PublicKey publicKey;

		public RSAPublic() {}

		public RSAPublic(RSAPrivate rsaPrivate) {
			setPublicKey(rsaPrivate.getPublicKeyEncoded());
		}

		public RSAPublic(byte[] encodedPublicKey) {
			setPublicKey(encodedPublicKey);
		}

		public String encryptRSA(String message) {
			return base64(encrypt("RSA/ECB/PKCS1Padding", publicKey, message.getBytes()));
		}

		public void getKey(Socket socket) {
			byte[] encodedPublicKey = null;
			try {
				int len;
				byte[] byteLength = new byte[4];
				len = socket.getInputStream().read(byteLength, 0, 4);
				if (len == 0) return;
				ByteBuffer bb = ByteBuffer.wrap(byteLength);
				len = bb.getInt();
				encodedPublicKey = new byte[len];
				len = socket.getInputStream().read(encodedPublicKey);
				if (len == 0) return;
			} catch (Exception ignored) { }
			setPublicKey(encodedPublicKey);
		}

		public boolean getVerification(Socket socket, String originalData) {
			Object obj = getUnencryptedObject(socket);
			return verify(originalData, (String) obj);
		}

		public void sendMessage(Socket socket, String message) {
			sendUnencryptedObject(socket, encryptRSA(message));
		}

		public void setPublicKey(byte[] encodedPublicKey) {
			try {
			/* rebuild key */
				X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encodedPublicKey);
				KeyFactory keyFactory = KeyFactory.getInstance("RSA");
				publicKey = keyFactory.generatePublic(pubKeySpec);
			} catch (Exception ignored) { }
		}

		public boolean verify(String originalData, String signature) {
			try {
			/* create a Signature object and initialize it with the public key */
				Signature sig = Signature.getInstance("SHA1withRSA");
				sig.initVerify(publicKey);

	            /* verify data */
				byte[] buffer = originalData.getBytes();
				sig.update(buffer, 0, buffer.length);

				/* done */
				return sig.verify(base64(signature));
			} catch (Exception ignored) { }
			return false;
		}
	}

	public static class SocketIOHandler {
		private final Socket         socket;
		private       BufferedReader in;
		private       PrintWriter    out;

		SocketIOHandler(Socket socket) {
			this.socket = socket;
			try {
				socket.setSoTimeout(2048);
				out = new PrintWriter(socket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			} catch (IOException ignored) { }
		}

		void close() {
			try {
				in.close();
				out.close();
				socket.close();
			} catch (IOException ignored) { }
		}

		String read() {
			String input = null;
			try {
				input = in.readLine();
			} catch (IOException ignored) { }
			return input;
		}

		void write(String message) {
			out.println(message);
			out.flush();
		}
	}

	public static class Stopwatch {
		private List<Long>   elapsedTimes;
		private long         startTime;
		private List<String> tags;

		Stopwatch() {
			init();
		}

		public final void init() {
			this.elapsedTimes = new LinkedList<Long>();
			this.tags = new LinkedList<String>();
			this.startTime = System.nanoTime();
		}

		public void log() {
			print(tags, elapsedTimes);
		}

		public void time() {
			time(String.format("Time #%d", this.tags.size() + 1));
		}

		public void time(String message) {
			this.elapsedTimes.add(System.nanoTime() - this.startTime);
			this.tags.add(message);
			print(String.format("%s : %d", tags.get(tags.size() - 1), elapsedTimes.get(elapsedTimes.size() - 1)));
		}
	}
}