echo "Opening tcp dump with wireshark..."
tail -c +1 -f dump/tcpdump.pcap | wireshark -k -i -