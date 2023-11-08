package net.helper;

import javax.xml.bind.DatatypeConverter;

public class StringHelper {

	public static String[] getDateParts(String date) {
		return date.split("-");
	}

	public static String convertBase64(byte[] avatar) {
		
		if (avatar == null || avatar.length == 0) {
			// return a default avatar
			return "iVBORw0KGgoAAAANSUhEUgAAAGAAAABgCAYAAADimHc4AAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAQ+SURBVHhe7Zu7axRRFMaj+AAfhXaCnQ8Qe20UwQcYsisK2ijubKJYRFNYCOLOOGAKNzOxECwEOwX1H3BXbSwsEkGUKDY+SKeCUSuRiMx6zuyRCXokbrJ372zu94OPLJvZe875ZvbOvXfv9AAAAAAAAAAAACCPJA/7NiX3i2eSeuEO6VlSK35OaoUfTdFrfo//x8fQsfIxMB8aT08tTe71lcjgJ416sdGK6GSM82cbj3YtkeZAK9CVXUzqxXeaua2ITsRb+lYUpFkwG41HR1aRabc0M+ejtE1qW8IAjaR+aB11Ny81A9shOgkTHEPCgZkktf3rqduZ1IxrpzgGx5KwgJFu54VmmAlxrOTBvpUSHpjo82cTdXU3Jbzb8AhFM6gTSuoH+iQNN+ExejuGmnNVGtvleQJ1A55mTCfFkzVJxz2o7x/XTOmkOAdJxy2Se72bNUNsiO5DGyUtd6Ar77Rmhg1xLpKWO9CE6LZmhg1xLpKWO9AI5Llmhg1xLpKWO/AavmaGDdE3YErScgfqd6c1M2yIc5G03AEnwDLogiyDm7BlMAy1DE9+NDNsiE7AoKTlDrlaiqgd3CBpuQV9C7AYZ5N0345iSifl9HK09R9kaoU3zm/cwk+SOYB/INcMMin8KD8DbEvJAdiYlQPSrYn1woRmXDvEbWNr4iwY25zL9xlszv1/eHSUDhMVM1sRt4HRzhxJ5wn8gMYcZsz8GTyg0UbSR5RqhUEyVh5RKkzR3+lUzdfNR5T4GBe3mQAAAAAAgHZwKgxXlC5Ut3kXR495fjTs+fFd+jtOelX248lyEH/xgugHi1/ze/y/5jHpscP8WW6D25Jmwb8YGrq6vFyJd5OZl8jUx2ToNL1utEXUVtomtc0xjoThMgnrOo1F5WBkpxfE1+mK/fyXcYbEsTgmx+YcJBl34C7Bq0RDZMQ7zaBOinPgXJzopgbOjawu+1FIRX/SzLApzokUDIbXFt6KaRiGi8v+yEA5iN5rxedLlCPlyjlL+t1Nya9upStrXC82v+KcOXcpo/vgK6jfj8/SCOS7VmBXiHLnGrruRn30/OU1dAXdV4vqTtW5Jikv3xz3q1toiPdaKaKrxTVxbVJmPumvjO6gUc5XrYAFIaqNa5Ry80UpiHs9P/6mJr6AxDXSvGG/lJ0P+ivR3rYuHeRd6dLG6B4p3y79QbzdhSv/T3HNXLvYYIeTYXW9F0QftQRdENfOHogdnYVXFGnWOKYl5paiMSurq3T2b+gJuSf2QmzpDDQ7PKwl4rLYE7HHLCfCK2tpFPBBS8JpkSfsjdhkDnQ9/1ZHuiK66fzUgkOs6KfYZA49MPRbYpM5tKBQJrHJHFpQKJPYZA4tKJRJbDKHFhTKJDaZQwsKZRKbzKEFhTKJTebQgkKZxCZzaEGhTGKTObSgUCaxyRxaUCiT2AQAAAAAAAAAAAAAwB/09PwCOopvwnzNF28AAAAASUVORK5CYII=";
		}
		return DatatypeConverter.printBase64Binary(avatar);
	}
	
	public static String convertImageBase64(byte[] image) {
        return DatatypeConverter.printBase64Binary(image);
    }
}
