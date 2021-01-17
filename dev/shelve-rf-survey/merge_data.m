close all; clear; clc;

sheet_names = {'4', '4A', '3', '3A', '2', '2A', '1', '1A'};

opts = detectImportOptions('rfsurvey.xlsx');

data = zeros(20, 7, length(sheet_names));

for i = 1 : length(sheet_names)
    opts.Sheet = sheet_names{i};
    data(:,:,i) = readmatrix('rfsurvey.xlsx', opts);
end

save('data.mat', 'data');