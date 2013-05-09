package practical2;

public class Parser {
	public static final int _EOF = 0;
	public static final int _ident = 1;
	public static final int _number = 2;
	public static final int _lpar = 3;
	public static final int _rpar = 4;
	public static final int _zerotoken = 5;
	public static final int _comma = 6;
	public static final int _period = 7;
	public static final int maxT = 12;

	static final boolean T = true;
	static final boolean x = false;
	static final int minErrDist = 2;

	public Token t;    // last recognized token
	public Token la;   // lookahead token
	int errDist = minErrDist;
	
	public Scanner scanner;
	public Errors errors;

	PhonebookFormatGenerator Phoneg;

/*-- Scanner specification --*/
	


	public Parser(Scanner scanner) {
		this.scanner = scanner;
		errors = new Errors();
	}

	void SynErr (int n) {
		if (errDist >= minErrDist) errors.SynErr(la.line, la.col, n);
		errDist = 0;
	}

	public void SemErr (String msg) {
		if (errDist >= minErrDist) errors.SemErr(t.line, t.col, msg);
		errDist = 0;
	}
	
	void Get () {
		for (;;) {
			t = la;
			la = scanner.Scan();
			if (la.kind <= maxT) {
				++errDist;
				break;
			}

			la = t;
		}
	}
	
	void Expect (int n) {
		if (la.kind==n) Get(); else { SynErr(n); }
	}
	
	boolean StartOf (int s) {
		return set[s][la.kind];
	}
	
	void ExpectWeak (int n, int follow) {
		if (la.kind == n) Get();
		else {
			SynErr(n);
			while (!StartOf(follow)) Get();
		}
	}
	
	boolean WeakSeparator (int n, int syFol, int repFol) {
		int kind = la.kind;
		if (kind == n) { Get(); return true; }
		else if (StartOf(repFol)) return false;
		else {
			SynErr(n);
			while (!(set[syFol][kind] || set[repFol][kind] || set[0][kind])) {
				Get();
				kind = la.kind;
			}
			return StartOf(syFol);
		}
	}
	
	void Phonebook() {
		while (la.kind == 1) {
			PersonalCard();
		}
	}

	void PersonalCard() {
		Expect(1);
		String lastname = t.val; Phoneg.PrintLastName(lastname); 
		Expect(6);
		FirstName();
		while (StartOf(1)) {
			PhoneType();
			PhoneNumber();
			Phoneg.NewLine(); 
		}
	}

	void FirstName() {
		String firstname, secondname = ""; boolean isPeriod = false; 
		Expect(1);
		firstname = t.val; 
		if (la.kind == 1) {
			Get();
			secondname = t.val; 
			if (la.kind == 7) {
				Get();
				isPeriod = true; 
			}
		}
		Phoneg.PrintFirstName(firstname, secondname, isPeriod); 
	}

	void PhoneType() {
		String phoneType = ""; 
		if (la.kind == 8 || la.kind == 9 || la.kind == 10) {
			if (la.kind == 8) {
				Get();
				phoneType = "office"; 
			} else if (la.kind == 9) {
				Get();
				phoneType = "mobile"; 
			} else {
				Get();
				phoneType = "home"; 
			}
		}
		Phoneg.PrintPhoneType(phoneType); 
	}

	void PhoneNumber() {
		String num = ""; boolean isCountryCode = false; boolean isCityCode = false; 
		if (la.kind == 11) {
			CountryCode();
			isCountryCode = true; isCityCode = true; 
		}
		Expect(2);
		num = t.val; Phoneg.CheckCityCode(num, isCountryCode, isCityCode); 
		while (la.kind == 2) {
			Get();
			num = t.val; Phoneg.PrintNumber(num, true, true); 
		}
	}

	void CountryCode() {
		Expect(11);
		Expect(2);
		String countryCodeFirst = t.val; 
		Phoneg.PrintCountryCode(countryCodeFirst); 
		Expect(3);
		Expect(5);
		Expect(4);
		Phoneg.PrintZero(); 
	}



	public void Parse() {
		la = new Token();
		la.val = "";		
		Get();
		Phonebook();
		Expect(0);

	}

	private static final boolean[][] set = {
		{T,x,x,x, x,x,x,x, x,x,x,x, x,x},
		{x,x,T,x, x,x,x,x, T,T,T,T, x,x}

	};
} // end Parser


class Errors {
	public int count = 0;                                    // number of errors detected
	public java.io.PrintStream errorStream = System.out;     // error messages go to this stream
	public String errMsgFormat = "-- line {0} col {1}: {2}"; // 0=line, 1=column, 2=text
	
	protected void printMsg(int line, int column, String msg) {
		StringBuffer b = new StringBuffer(errMsgFormat);
		int pos = b.indexOf("{0}");
		if (pos >= 0) { b.delete(pos, pos+3); b.insert(pos, line); }
		pos = b.indexOf("{1}");
		if (pos >= 0) { b.delete(pos, pos+3); b.insert(pos, column); }
		pos = b.indexOf("{2}");
		if (pos >= 0) b.replace(pos, pos+3, msg);
		errorStream.println(b.toString());
	}
	
	public void SynErr (int line, int col, int n) {
		String s;
		switch (n) {
			case 0: s = "EOF expected"; break;
			case 1: s = "ident expected"; break;
			case 2: s = "number expected"; break;
			case 3: s = "lpar expected"; break;
			case 4: s = "rpar expected"; break;
			case 5: s = "zerotoken expected"; break;
			case 6: s = "comma expected"; break;
			case 7: s = "period expected"; break;
			case 8: s = "\"office\" expected"; break;
			case 9: s = "\"mobile\" expected"; break;
			case 10: s = "\"home\" expected"; break;
			case 11: s = "\"+\" expected"; break;
			case 12: s = "??? expected"; break;
			default: s = "error " + n; break;
		}
		printMsg(line, col, s);
		count++;
	}

	public void SemErr (int line, int col, String s) {	
		printMsg(line, col, s);
		count++;
	}
	
	public void SemErr (String s) {
		errorStream.println(s);
		count++;
	}
	
	public void Warning (int line, int col, String s) {	
		printMsg(line, col, s);
	}
	
	public void Warning (String s) {
		errorStream.println(s);
	}
} // Errors


class FatalError extends RuntimeException {
	public static final long serialVersionUID = 1L;
	public FatalError(String s) { super(s); }
}
