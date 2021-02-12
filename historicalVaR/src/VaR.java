import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class VaR {

    public static final double EPSILON = 0.001;

    private final Double confidenceLevel;
    private final Double investment;
    private final VaRValue value;

    public VaRValue getValue() {
        return value;
    }

    public VaR(Double confidenceLevel, Double investment){
        this.confidenceLevel = confidenceLevel;
        this.investment = investment;
        value = new VaRValue();
    }

    public void calculateHistoricalVaR(List<Double> prices) {

        try {

            checkParameters();
            if (value.getErrorMessage() != null) return;

            List<Double> historicalReturns = calculateHistoricalReturnList(prices);

            int numberOfDays = historicalReturns.size();
            if (numberOfDays<2){
                value.setErrorMessage("The number of data is <2. Please select a valid column/file to compute the VaR.");
                return;
            }

            historicalReturns.sort(Double::compareTo);
            int indexVaR = (int) (numberOfDays*(1-confidenceLevel/100)) - 1;
            value.setValue(historicalReturns.get(indexVaR)*investment);


        } catch (IllegalArgumentException ignored) {}

    }

    private void checkParameters() {

        if ((confidenceLevel == null) || (confidenceLevel<EPSILON) || (100-confidenceLevel<EPSILON)){
            value.setErrorMessage("Please enter a valid confidence level value between 0.001 and 99.999.");
        }
        else if ((investment == null) || !(0<investment)){
            value.setErrorMessage("Please enter a valid investment value (>0).");
        }
    }


    private List<Double> calculateHistoricalReturnList(List<Double> prices) throws IllegalArgumentException {

        List<Double> returns = new ArrayList<>();

        Iterator<Double> it = prices.iterator();
        Double previousPrice;
        Double nextPrice = null;

        if (it.hasNext()){
            nextPrice = it.next();
        }

        while (it.hasNext()){
            previousPrice = nextPrice;
            nextPrice = it.next();
            returns.add((nextPrice-previousPrice)/Math.abs(previousPrice));
        }

        if (returns.isEmpty()){
            value.setErrorMessage("The CSV file must contain at least two rows.");
            throw new IllegalArgumentException();
        }

        return returns;
    }


}
