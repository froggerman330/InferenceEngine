/**
 * 
 */
package logic.operator;

import logic.Logic;

/**
 * @author Devon
 *
 */
public interface Operator extends Logic
{
    public Logic getPremise();

    public Logic getConclusion();
}
