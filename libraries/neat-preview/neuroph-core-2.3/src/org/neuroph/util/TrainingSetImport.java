/***
 * Neuroph  http://neuroph.sourceforge.net
 * Copyright by Neuroph Project (C) 2008
 *
 * This file is part of Neuroph framework.
 *
 * Neuroph is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * Neuroph is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Neuroph. If not, see <http://www.gnu.org/licenses/>.
 */

package org.neuroph.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import org.neuroph.core.learning.SupervisedTrainingElement;
import org.neuroph.core.learning.TrainingElement;
import org.neuroph.core.learning.TrainingSet;


/**
 * Handles training set imports
 *
 * @author Zoran Sevarac
 * @author Ivan Nedeljkovic
 * @author Kokanovic Rados
 */

// TODO: importFromDatabase(sql, ...) and importFromUrl(url, ...)
public class TrainingSetImport
{
  
  public static TrainingSet importFromFile(String filePath, int inputsCount, int outputsCount, String separator)
    throws IOException, FileNotFoundException, NumberFormatException
  {

    FileReader fileReader = null;

    try {
     TrainingSet trainingSet = new TrainingSet();
     fileReader = new FileReader(new File(filePath));
     BufferedReader reader = new BufferedReader(fileReader);

     String line = "";
      
      while((line = reader.readLine())!=null) {
        Vector<Double> inputs = new Vector<Double>();
        Vector<Double> outputs = new Vector<Double>();
        String[] niz = line.split(separator);

        for (int i = 0; i < inputsCount; i++)
          inputs.add(Double.valueOf(Double.parseDouble(niz[i])));

        for (int i = inputsCount; i < inputsCount + outputsCount; i++)
          outputs.add(Double.valueOf(Double.parseDouble(niz[i])));

        if (outputsCount>0) {
            trainingSet.addElement(new SupervisedTrainingElement(inputs, outputs))  ;
        } else {
            trainingSet.addElement(new TrainingElement(inputs));
        }
      }

      return trainingSet;
      
    } catch (FileNotFoundException ex) {
       ex.printStackTrace();
       throw ex;
    } catch(IOException ex) {
    	if(fileReader != null) {
    		fileReader.close();
    	}
    	ex.printStackTrace();
    	throw ex;
    } catch (NumberFormatException ex) {
       fileReader.close();
       ex.printStackTrace();
       throw ex;
    }
  }

}