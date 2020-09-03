import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class RCA_Corporate_Member_Companies_Profile_Scraper {

    public static void main(String[] args) {
        String rCSACompaniesListText = readRCSAData();
        if(rCSACompaniesListText!=null){
            String[] tableRows = rCSACompaniesListText.split("</tr>");
            for (int index=0; index< tableRows.length; index++) {
                String row = tableRows[index];
                if(row.contains("href=\"")){
                    String companyProfileLink = row.substring(
                      row.indexOf("href=\"")+6,      row.indexOf("\">", row.indexOf("href=\""))
                    );
                    companyProfileLink = "https://www.rcsa.com.au" + companyProfileLink;
                    try {
                        String pageContent = getPageContent(companyProfileLink);
                        processCompanyPageContent(index, companyProfileLink, pageContent);
                    }catch (Exception e) {
                        System.err.println("Got Exception on index : " + index + " \t Page link: " + companyProfileLink );
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static void processCompanyPageContent(int index, String companyProfileLink, String pageContent) {
        String companyTitle = "";
        if(pageContent.contains("PanelFieldValue\">")){
            int titleStartIndex = pageContent.indexOf("\">", pageContent.indexOf("PanelFieldValue\">") + 17)+2;
            int titleEndIndex = pageContent.indexOf("</span>", pageContent.indexOf("PanelFieldValue\">") + 17);
            companyTitle = pageContent.substring(titleStartIndex, titleEndIndex);
        }
        String specialization = "";
        if(pageContent.contains("<label>SPECIALISATION:</label><br />")){
            int specializationStartIndex = pageContent.indexOf("<label>SPECIALISATION:</label><br />") +36;
            int specializationEndIndex = pageContent.indexOf("</p>", specializationStartIndex);
            specialization = pageContent.substring(specializationStartIndex, specializationEndIndex);
        }
        String phone = "";
        if(pageContent.contains("<label>Phone:</label>")){
            int phoneStartIndex = pageContent.indexOf("<label>Phone:</label>") +21;
            int phoneEndIndex = pageContent.indexOf("</p>", phoneStartIndex);
            phone = pageContent.substring(phoneStartIndex, phoneEndIndex);
        }

        String website = "";
        if(pageContent.contains("<label>Website:</label> <a href=\"")){
            int websiteStartIndex = pageContent.indexOf("<label>Website:</label> <a href=\"") +33;
            int websiteEndIndex = pageContent.indexOf("\" ", websiteStartIndex);
            website = pageContent.substring(websiteStartIndex, websiteEndIndex);
        }
        System.out.println(index
        + "^^" + companyTitle.trim()  + "^^" + specialization.trim() + "^^" + phone.trim() + "^^"
                        + (website.trim().length()>7 ? website.trim() : "")
        );
    }

    private static String readRCSAData() {
        try {
            return new String(Files.readAllBytes(Paths.get(RCA_Corporate_Member_Companies_Profile_Scraper.class.getResource("RCSA_LIST.txt").toURI())));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getPageContent(String link) throws IOException {
        URL url = new URL(link);
        InputStream is = url.openStream();
        int ptr = 0;
        StringBuffer buffer = new StringBuffer();
        while ((ptr = is.read()) != -1) {
            buffer.append((char)ptr);
        }
        return buffer.toString();

    }

}
