package org.idempierelbr.tax.model;

import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.Query;

public class MLBRDocLinePIS extends X_LBR_DocLine_PIS {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8383356153009002225L;

	/**
	 *  Default Constructor
	 *  @param Properties ctx
	 *  @param int ID (0 create new)
	 *  @param String trx
	 */
	public MLBRDocLinePIS(Properties ctx, int LBR_DocLine_PIS_ID,
			String trxName) {
		super(ctx, LBR_DocLine_PIS_ID, trxName);
	}
	
	/**
	 *  Load Constructor
	 *  @param ctx context
	 *  @param rs result set record
	 *  @param trxName transaction
	 */
	public MLBRDocLinePIS(Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}
	
	/**
	 * 	Get PIS array of a given Doc Line Details.
	 *	@return PIS array
	 */
	public static MLBRDocLinePIS[] getOfDetails (Properties ctx, int LBR_DocLine_Details_ID, String trxName) {	
		MLBRDocLineDetails details = new MLBRDocLineDetails(ctx, LBR_DocLine_Details_ID, trxName);
		return getOfDetails(details);
	}	
	
	/**
	 * 	Get PIS array of a given Doc Line Details.
	 *	@return PIS array
	 */
	public static MLBRDocLinePIS[] getOfDetails (MLBRDocLineDetails details) {	
		if (details == null)
			return null;
		
		List<MLBRDocLinePIS> list = new Query (details.getCtx(), MLBRDocLinePIS.Table_Name,
				"LBR_DocLine_Details_ID=?", details.get_TrxName())
			.setParameters(new Object[]{details.get_ID()})
			.list();
		
		return list.toArray(new MLBRDocLinePIS[list.size()]);	
	}
	
	/**
	 * 	Copy PIS from a details to another details
	 *	@return true if copied ok
	 */
	public static boolean copy(MLBRDocLineDetails detailsFrom, MLBRDocLineDetails detailsTo) {
		MLBRDocLinePIS[] pisLinesFrom = getOfDetails(detailsFrom);
		MLBRDocLinePIS[] pisLinesTo = getOfDetails(detailsTo);
		
		if (pisLinesFrom.length == 0 || pisLinesTo.length > 0)
			return false;
		
		MLBRDocLinePIS pisFrom = pisLinesFrom[0];
		MLBRDocLinePIS pisTo = new MLBRDocLinePIS(detailsTo.getCtx(), 0, detailsTo.get_TrxName());
		MLBRDocLinePIS.copyValues(pisFrom, pisTo, detailsTo.getAD_Client_ID(), detailsTo.getAD_Org_ID());
		pisTo.setLBR_DocLine_Details_ID(detailsTo.get_ID());
		
		try {
			pisTo.saveEx();
			return true;
		} catch (AdempiereException e) {
			return false;
		}
	}

}