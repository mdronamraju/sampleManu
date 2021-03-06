package us.co.douglas.assessor.rest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import us.co.douglas.assessor.model.BasicAccountInfo;
import us.co.douglas.assessor.model.Parcel;
import us.co.douglas.assessor.model.NeighborhoodSale;
import us.co.douglas.assessor.service.AccountService;
import us.co.douglas.assessor.service.AccountServiceImpl;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mdronamr on 12/22/15.
 */

@Path("/accountService")
@Consumes("application/json")
@Produces("application/json")
public class AccountRestService {
    private static Log log = LogFactory.getLog(AccountRestService.class);
    private AccountService accountService = new AccountServiceImpl();
    private static int maxRows = 2000;
    private static Boolean matchAnyWhere = false;
    private static Boolean matchAll = true;
    private static List<BasicAccountInfo> allSearchableParcels = null;

    @GET
    @Path("/allSearchableStrings/{searchString}")
    public List<BasicAccountInfo> getAllSearchableStrings(@PathParam("searchString") String searchString) throws Exception {
        log.info("getAllSearchableStrings()...");
        log.info("searchString: " + searchString);
        //log.info("searchCriteria: " + searchCriteria);
        String[] stringTokens =  searchString.split(" ");
        List<BasicAccountInfo> matchedParcels = new ArrayList<BasicAccountInfo>();
        List<String> allSearchableStrings = accountService.getAllSearchableStrings();
        log.info("allSearchableStrings.size(): " + allSearchableStrings.size());
        if (allSearchableStrings != null & allSearchableStrings.size() > 0) {
            for (String searchableString : allSearchableStrings) {
                if (matchAnyWhere) { //If any string matches with any of the columns
                    for (String stringToken : stringTokens) {
                        if ((searchableString.toUpperCase().contains(stringToken.toUpperCase()))) {
                            BasicAccountInfo basicAccountInfo = new BasicAccountInfo();
                            String[] searchableStringTokens = searchableString.split(":");
                            basicAccountInfo.setAccountNo(searchableStringTokens[0]);
                            basicAccountInfo.setParcelNo(searchableStringTokens[1]);
                            basicAccountInfo.setOwnerName(searchableStringTokens[2]);
                            basicAccountInfo.setBusinessName(searchableStringTokens[3]);
                            basicAccountInfo.setBusinessLicense(searchableStringTokens[4]);
                            basicAccountInfo.setNeighborhoodCode(searchableStringTokens[5]);
                            basicAccountInfo.setNeighborhoodExt(searchableStringTokens[6]);
                            basicAccountInfo.setPropertyStreet(searchableStringTokens[7]);
                            basicAccountInfo.setPropertyCity(searchableStringTokens[8]);
                            basicAccountInfo.setPropertyState("CO");
                            basicAccountInfo.setPropertyZipCode(searchableStringTokens[9]);
                            basicAccountInfo.setSubdivisionName(searchableStringTokens[10]);

                            if (!matchedParcels.contains(basicAccountInfo)) {
                                matchedParcels.add(basicAccountInfo);
                            }
                            break;
                        }
                    }
                } else if (matchAll) { //If all the strings match with any of the columns
                    Boolean stringMatches = true;
                    for (String stringToken : stringTokens) {
                        if (!(searchableString.toUpperCase().contains(stringToken.toUpperCase()))) {
                            stringMatches = false;
                            break;
                        }
                    }
                    if (stringMatches) {
                        BasicAccountInfo basicAccountInfo = new BasicAccountInfo();
                        String[] searchableStringTokens = searchableString.split(":");
                        basicAccountInfo.setAccountNo(searchableStringTokens[0]);
                        basicAccountInfo.setParcelNo(searchableStringTokens[1]);
                        basicAccountInfo.setOwnerName(searchableStringTokens[2]);
                        basicAccountInfo.setBusinessName(searchableStringTokens[3]);
                        basicAccountInfo.setBusinessLicense(searchableStringTokens[4]);
                        basicAccountInfo.setNeighborhoodCode(searchableStringTokens[5]);
                        basicAccountInfo.setNeighborhoodExt(searchableStringTokens[6]);
                        basicAccountInfo.setPropertyStreet(searchableStringTokens[7]);
                        basicAccountInfo.setPropertyCity(searchableStringTokens[8]);
                        basicAccountInfo.setPropertyState("CO");
                        basicAccountInfo.setPropertyZipCode(searchableStringTokens[9]);
                        basicAccountInfo.setSubdivisionName(searchableStringTokens[10]);
                        if (!matchedParcels.contains(basicAccountInfo)) {
                            matchedParcels.add(basicAccountInfo);
                        }
                    }
                }

                if (matchedParcels.size() >= maxRows) {
                    log.info("Max rows(" + maxRows + ") reached. Stopping the search...");
                    break;
                }
            }
        }
        log.info("ONLY FIRST " + maxRows + " ROWS WILL BE DISPLAYED!");
        log.info("matchedParcels.size(): " + matchedParcels.size());
        return matchedParcels;
    }

    @GET
    @Path("/parcels/{accountNo}")
    public Parcel getParcel(@PathParam("accountNo") String accountNo) throws Exception {
        log.info("getParcel()...");
        log.info("accountNo: " + accountNo);
        return accountService.getParcel(accountNo);
    }

    @GET
    @Path("/neighborhoodSales/{zipCode}/{neighborhood}/{neighborhoodExt}/{subdivision}")
    public List<NeighborhoodSale> getNeighborhoodSales(@PathParam("zipCode") String zipCode, @PathParam("neighborhood") String neighborhood, @PathParam("neighborhoodExt") String neighborhoodExt, @PathParam("subdivision") String subdivision) throws Exception {
        log.info("getNeighborhoodSales()...");
        List<NeighborhoodSale> allNeighborhoodSales = accountService.getAllNeighborhoodSales();
        List<NeighborhoodSale> matchedNeighborhoodSales = new ArrayList<NeighborhoodSale>();
        for (NeighborhoodSale neighborhoodSale : allNeighborhoodSales) {
            if (neighborhoodSale.getPropertyZipCode().equalsIgnoreCase(zipCode) &&
                    neighborhoodSale.getNeighborhood().equalsIgnoreCase(neighborhood) &&
                    neighborhoodSale.getNeighborhoodExt().equalsIgnoreCase(neighborhoodExt) &&
                    neighborhoodSale.getSubdivision().equalsIgnoreCase(subdivision)) {
                matchedNeighborhoodSales.add(neighborhoodSale);
            }
            if (matchedNeighborhoodSales.size() >= maxRows) {
                log.info("Max rows(" + maxRows + ") reached. Stopping the search...");
                break;
            }
        }
        return matchedNeighborhoodSales;
    }
}