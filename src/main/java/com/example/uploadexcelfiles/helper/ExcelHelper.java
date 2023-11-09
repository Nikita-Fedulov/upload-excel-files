package com.example.uploadexcelfiles.helper;

public class ExcelHelper {
//    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
//    static String[] HEADERs = { "Id", "Title" };
//    static String SHEET = "Adress";
//
//    public static boolean hasExcelFormat(MultipartFile file) {
//        if (!TYPE.equals(file.getContentType())) {
//            return false;
//        }
//        return true;
//    }
//    public static ByteArrayInputStream tutorialsToExcel(List<Address> tutorials) {
//
//        try (Workbook workbook = new XSSFWorkbook();
//             ByteArrayOutputStream out = new ByteArrayOutputStream();) {
//            Sheet sheet = workbook.createSheet(SHEET);
//
//            // Header
//            Row headerRow = sheet.createRow(0);
//
//            for (int col = 0; col < HEADERs.length; col++) {
//                Cell cell = headerRow.createCell(col);
//                cell.setCellValue(HEADERs[col]);
//            }
//
//            int rowIdx = 1;
//            for (Address tutorial : tutorials) {
//                Row row = sheet.createRow(rowIdx++);
//                row.createCell(0).setCellValue(tutorial.getId());
//                row.createCell(1).setCellValue(tutorial.getAdress());
//            }
//
//            workbook.write(out);
//            return new ByteArrayInputStream(out.toByteArray());
//        } catch (IOException e) {
//            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
//        }
//    }
//
//    public static List<Address> excelToTutorials(InputStream is) {
//        try {
//            Workbook workbook = new XSSFWorkbook(is);
//
//            Sheet sheet = workbook.getSheet(SHEET);
//            Iterator<Row> rows = sheet.iterator();
//
//            List<Address> tutorials = new ArrayList<Address>();
//
//            int rowNumber = 0;
//            while (rows.hasNext()) {
//                Row currentRow = rows.next();
//
//                // skip header
//                if (rowNumber == 0) {
//                    rowNumber++;
//                    continue;
//                }
//
//                Iterator<Cell> cellsInRow = currentRow.iterator();
//
//                Address tutorial = new Address();
//
//                int cellIdx = 0;
//                while (cellsInRow.hasNext()) {
//                    Cell currentCell = cellsInRow.next();
//
//                    switch (cellIdx) {
//                        case 0:
//                            tutorial.setId((long) currentCell.getNumericCellValue());
//                            break;
//
//                        case 1:
//                            tutorial.setAdress(currentCell.getStringCellValue());
//                            break;
//                    }
//
//                    cellIdx++;
//                }
//
//                tutorials.add(tutorial);
//            }
//
//            workbook.close();
//
//            return tutorials;
//        } catch (IOException e) {
//            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
//        }
//    }
}
