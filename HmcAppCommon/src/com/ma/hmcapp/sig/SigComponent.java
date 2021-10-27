package com.ma.hmcapp.sig;

import java.security.GeneralSecurityException;

import org.springframework.stereotype.Component;

import com.ma.common.rsa.Sig;
import com.ma.common.rsa.SigSHA256WithRSA;

@Component
public class SigComponent implements Sig {
	private static String publicKeyStr = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJTCwq+GoGILQIq2IDw0QTtmFMoPfnG5dOmDBDk5uKPKkJB/5vW42SAa+QCp0Lvm/uWT49vBSlihcIPnYQDXLWkCAwEAAQ==";
	private static String privateKeyStr = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAlMLCr4agYgtAirYgPDRBO2YUyg9+cbl06YMEOTm4o8qQkH/m9bjZIBr5AKnQu+b+5ZPj28FKWKFwg+dhANctaQIDAQABAkAv0AlS8SpLWOJ9stvZfdIZjU3RQ/mYA8uX4gxReEPoII1XP8qbw/bcavfQoSMtc9hWL2PyU7pVpRLNcPSaqwshAiEA0pVcL9yx51qnwUSlrey7DcD0HcsjGJk0BT0bmJVbscMCIQC02BNC3h/mZBplxsv9S+0o7U4Toa9xtO8ddsfeckrlYwIhAKtfbbRtsEINHrRD/2j5XIim3INai2c7VoBk6I5WDlctAiBHwu5xvF8AN3zaIjbUPorPkMtIswJBpsCz+mx5cvURbQIgIa9Z8FVv7xQimiasfFkR4og/BwCPgMj8qP0t157QNtM=";

	private SigSHA256WithRSA sig;

	public SigComponent() {
		try {
			sig = new SigSHA256WithRSA(privateKeyStr, publicKeyStr);
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}
	}

	@Override
	public byte[] sign(byte[] data) throws GeneralSecurityException {
		return sig.sign(data);
	}

	@Override
	public boolean check(byte[] data, byte[] signature) throws GeneralSecurityException {
		return sig.check(data, signature);
	}

}
