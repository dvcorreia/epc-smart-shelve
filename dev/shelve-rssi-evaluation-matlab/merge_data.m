clear all; close all; clc;

% Get all .cvs files from data directory
files = dir('data/*.csv');

% Parse data in to matlab structures
data = zeros(3,6,3);

for i = 1 : length(files)
   file_name = files(i).name;
   file_data = csvread(append('data/', file_name));
   data_mean = mean(file_data(:,1));
   data(str2double(file_name(5))+1, str2double(file_name(4))+1, str2double(file_name(3))+1) = data_mean;
end
    
save('data.mat', 'data');