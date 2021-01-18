close all; clear; clc;
load('data.mat');

data(isnan(data)) = Inf;
data = -1 * data;

x = linspace(0, 150, size(data, 1));
y = linspace(0, 60, size(data, 2));
z = [0, 250, 500, 700, 950, 1200, 1430, 1600];

fig1 = figure('Position',[100 100 500 400]); 
hold on; grid minor;
set(gca, 'XMinorGrid', 'on')
set(gca, 'YMinorGrid', 'on')
view(-16.5,20);

title('Obstruction Points','Interpreter','latex')
xlabel('x (cm)','Interpreter','latex')
ylabel('y (cm)','Interpreter','latex')
zlabel('z (cm)','Interpreter','latex')

[X, Y, Z] = meshgrid(x, y, z);

for i = 1 : length(z)
    XX = X(:,:,i);
    YY = Y(:,:,i); 
    ZZ = Z(:,:,i);
    DATA = data(:,:,i)';
    id = DATA < -80;
    fig1(1) = scatter3(XX(id), YY(id), ZZ(id), 30, 'filled', 'b');
    fig1(2) = scatter3(XX(id), YY(id), zeros(size(XX(id))), 10, 'r', 'x');
end

% Draw metal supports
for i = 1 : length(z)/2
    fig1(3) = plot3([50 50],[0 max(y)],[z(i*2-1) z(i*2-1)], 'k','LineWidth',1.5);
    fig1(3) = plot3([110 110],[0 max(y)],[z(i*2-1) z(i*2-1)], 'k', 'LineWidth',1.5);
end

legend(fig1,'Obstruction Points', 'Obstruction Points z=0', 'Support metal bars', 'Location','southeast', 'Interpreter','latex');
