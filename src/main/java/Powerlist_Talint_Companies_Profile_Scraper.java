import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class Powerlist_Talint_Companies_Profile_Scraper {
    
    //The Powerlist
    //The Power List helps the UK's top 500 recruitment companies to differentiate themselves from thousands of competitors.
    //URL: https://powerlist.talint.co.uk/
    
    public static void main(String[] args) {
        for (int pageNumber =1; pageNumber<=51; pageNumber++){
            try {
                //System.out.println("Page number: " + pageNumber);
                String pageContent = getPageContent("https://powerlist.talint.co.uk/talint-powerlist/search-companies/?sf_paged=" + pageNumber);
                String [] linkSections = pageContent.split("</a></h2>");
                for (String linkSection: linkSections) {
                    if(linkSection.contains("<h2><a href=\"")){
                        String companyLink = linkSection.substring(linkSection.indexOf("<h2><a href=\"")+13,
                                linkSection.lastIndexOf("/\">"));
                        String companyDetailsContent = getPageContent(companyLink);
                        processCompanyDetails(companyLink, companyDetailsContent);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void processCompanyDetails(String companyLink, String companyDetailsContent) {
        String companyName = "";
        if(companyDetailsContent.contains("div50nb\">\n" +
                "<h3>"))
            companyName = companyDetailsContent.substring(
              companyDetailsContent.indexOf("div50nb\">\n" +
                      "<h3>") + 14,
                    companyDetailsContent.indexOf("</h3>")
            );
        String numberOfEmployees = "";
        if(companyDetailsContent.contains("<p>Number of Employees:"))
            numberOfEmployees = companyDetailsContent.substring(
                    companyDetailsContent.indexOf("<p>Number of Employees:") + 23,
                    companyDetailsContent.indexOf("<br", companyDetailsContent.indexOf("<p>Number of Employees:") )
            );
        String telephone = "";
        if(companyDetailsContent.contains("</prefix>\n" +
                "Telephone:"))
            telephone = companyDetailsContent.substring(
                    companyDetailsContent.indexOf("</prefix>\n" +
                            "Telephone:") + 20,
                    companyDetailsContent.indexOf("<br", companyDetailsContent.indexOf("</prefix>\n" +
                            "Telephone:") )
            );

        String email = "";
        if(companyDetailsContent.contains("/>\n" +
                "Email:"))
            email = companyDetailsContent.substring(
                    companyDetailsContent.indexOf("/>\n" +
                            "Email:") + 9,
                    companyDetailsContent.indexOf("<br",     companyDetailsContent.indexOf("/>\n" +
                            "Email:") )
            );

        String website = "";
        if(companyDetailsContent.contains("Website: <a href=\""))
            website = companyDetailsContent.substring(
                    companyDetailsContent.indexOf("Website: <a href=\"") + 18,
                    companyDetailsContent.indexOf("\" target=",     companyDetailsContent.indexOf("Website: <a href=\"") )
            );

        String headOfficeAddress = "";
        if(companyDetailsContent.contains("<prefix>Head Office Address</prefix>"))
            headOfficeAddress = companyDetailsContent.substring(
                    companyDetailsContent.indexOf("<prefix>Head Office Address</prefix>") + 36,
                    companyDetailsContent.indexOf("<prefix>Contact Information</prefix>")
            );
        headOfficeAddress = headOfficeAddress.replaceAll("<br />", "");


        System.out.println(companyName.trim() + "^^" + numberOfEmployees.trim() + "^^" +
                telephone.trim() + "^^" + email.trim() + "^^" + website.trim()  + "^^" +
                headOfficeAddress.replaceAll("\n"," ").trim() +  "^^" +    companyLink
                );

    }

    private static String getPageContent(String link) throws IOException {
        URL   url = new URL(link);
            InputStream is = url.openStream();
            int ptr = 0;
            StringBuffer buffer = new StringBuffer();
            while ((ptr = is.read()) != -1) {
                buffer.append((char)ptr);
            }
            return buffer.toString();

    }
}
