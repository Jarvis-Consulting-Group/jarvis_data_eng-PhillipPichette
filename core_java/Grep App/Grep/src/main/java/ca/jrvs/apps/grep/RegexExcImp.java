package ca.jrvs.apps.grep;

import ca.jrvs.apps.grep.util.RegexExc;

public class RegexExcImp implements RegexExc {

    @Override
    public boolean matchJpeg(String filename) {
        return filename.matches("^.+\\.(?i)(jpeg|jpg)$");
    }

    @Override
    public boolean matchIp(String ip) {
        return ip.matches("^([0-9]{1,3}(\\.[0-9]{1,3}){3})$");
    }

    @Override
    public boolean isEmptyLine(String line) {
        return line.matches("^$");
    }
}
