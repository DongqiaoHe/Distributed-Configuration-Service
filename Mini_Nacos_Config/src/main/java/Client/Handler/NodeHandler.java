package Client.Handler;

import Client.Model.DataEntry;
import Client.Model.Node;
import Message.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

import static Const.MessageConst.UNCOMMITTED;

/**
 * @author hedongqiao
 * @date 2024/05/05
 */
public class NodeHandler extends SimpleChannelInboundHandler<Message> {

    Node node;

    ArrayList<String> commits = new ArrayList<>();

    Scanner scanner = new Scanner(System.in);

    Thread inputThread;

    /**
     * @param node
     */
    public NodeHandler(Node node) {
        this.node = node;

    }

    /**
     * @param ctx channel handler context
     * @param msg message
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        if (msg instanceof DataMessage) {
            DataMessage dataMessage = (DataMessage) msg;
            node.setUid(dataMessage.getUid());
            System.out.println(dataMessage.getData());
        }

        if (msg instanceof CandidateOptionsMessage) {
            CandidateOptionsMessage message = (CandidateOptionsMessage) msg;
            StringBuffer sb = new StringBuffer();
            String selectedId = "";
            sb.append("===========================\n");
            sb.append("Leader is disconnected.\n");
            sb.append("Election selection started.\n");
            sb.append("Candidate Options: \n");
            HashSet<String> ids = message.getIds();
            for (String id : ids) {
                sb.append(id);
                sb.append(", ");
                if (selectedId.compareTo(id) < 0) {
                    selectedId = id;
                }
            }
            System.out.println(sb);
            System.out.println("Selected candidate: " + selectedId);
            ctx.writeAndFlush(new ElectionMessage(selectedId, "It has the biggest ID."));
        }

        if (msg instanceof ElectionMessage) {
            ElectionMessage electionMessage = (ElectionMessage) msg;
            String leaderId = electionMessage.getLeaderId();
            node.setLeaderId(leaderId);
            System.out.println(electionMessage.getMessage());
            if (node.getUid().equals(node.getLeaderId())) {
                System.out.println("You are elected as the leader!");
            } else {
                System.out.println("You am not the leader!");
            }

            if (inputThread != null && inputThread.isAlive()) {
                inputThread.interrupt();
            }
            System.out.println("==================================");
            System.out.println("   Input instructions:");
            System.out.println("    get [Key] ");
            if (node.getUid().equals(node.getLeaderId())) {
                System.out.println("    set [Key] [Value]");
                System.out.println("    remove [Key] ");
            }
            System.out.println("    quit ");
            System.out.println("==================================");
            inputThread = new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    String command = null;
                    try {
                        synchronized (scanner) {
                            if (scanner.hasNextLine()) {
                                command = scanner.nextLine();
                            }
                        }
                    } catch (Exception e) {
                        continue;
                    }
                    String[] s = command.split(" ");
                    if (command != null && !checkInput(s)) {
                        System.out.println("Invalid input!");
                        continue;
                    }

                    String key;
                    DataEntry dataEntry;
                    switch (s[0]) {
                        case "get":
                            dataEntry = node.getDataEntry(s[1]);
                            if (dataEntry != null) {
                                System.out.println(dataEntry.getValue());
                            } else {
                                System.out.println("Data not found!");
                            }
                            break;

                        case "set":
                            if (!node.getUid().equals(node.getLeaderId())) {
                                System.out.println("Failure to add. You are not the leader node!");
                                break;
                            }
                            key = s[1];
                            dataEntry = new DataEntry(key, s[2], UNCOMMITTED);
                            node.addData(key, dataEntry);
                            ctx.writeAndFlush(new IncrementalDataMessage(s[1], s[2], UNCOMMITTED));
                            break;

                        case "remove":
                            if (!node.getUid().equals(node.getLeaderId())) {
                                System.out.println("Failure to remove. You are not the leader node!");
                                break;
                            }
                            ctx.writeAndFlush(new RemoveDataMessage(s[1]));
                            break;
                        case "quit":
                            ctx.channel().close();
                            return;
                    }
                }
            }, "in");
            inputThread.start();
        }

        if (msg instanceof IncrementalDataMessage) {
            IncrementalDataMessage incrementalDataMessage = (IncrementalDataMessage) msg;
            DataEntry dataEntry = new DataEntry(incrementalDataMessage.getKey(), incrementalDataMessage.getValue(), incrementalDataMessage.getState());
            node.addData(incrementalDataMessage.getKey(), dataEntry);
            if (incrementalDataMessage.getState() == UNCOMMITTED) {
                System.out.println("Data key: " + incrementalDataMessage.getKey() + "|| Value: " + incrementalDataMessage.getValue() + " || state: " + incrementalDataMessage.getStrState());
                ctx.writeAndFlush(new AckDataMessage(incrementalDataMessage.getKey(), node.getUid()));
            }
        }

        if (msg instanceof FullDataMessage) {
            FullDataMessage fullDataMessage = (FullDataMessage) msg;
            node.setDataset(fullDataMessage.getDataset());
            System.out.println("Data set updated.");
        }

        if (msg instanceof AddNodeMessage) {
            AddNodeMessage addNodeMessage = (AddNodeMessage) msg;
            ctx.writeAndFlush(new FullDataMessage(addNodeMessage.getNewId(), node.getDataset()));
        }

        if (msg instanceof AckDataMessage) {
            AckDataMessage ackDataMessage = (AckDataMessage) msg;
            if (node.getLeaderId().equals(node.getUid())) {
                node.addAck(ackDataMessage.getKey());
                System.out.println("Ack Key: " + ackDataMessage.getKey() + " from " + ackDataMessage.getSenderId());
                String key = ackDataMessage.getKey();
                if (!commits.contains(key) && node.getDataAck(key) > ackDataMessage.getChannelNum() / 2) {
                    //领导者发动提交信息。
                    commits.add(key);
                    System.out.println("More than half followers have acked.");
                    ctx.writeAndFlush(new CommitDataMessage(ackDataMessage.getKey()));
                }
            }
        }

        if (msg instanceof CommitDataMessage) {
            CommitDataMessage commitDataMessage = (CommitDataMessage) msg;
            String key = commitDataMessage.getKey();
            DataEntry dataEntry = node.getDataEntry(key);
            node.commitData(key);
            System.out.println("Data key: " + commitDataMessage.getKey() + "|| Value: " + dataEntry.getValue() + " || state: " + dataEntry.getState());
        }

        if (msg instanceof RemoveDataMessage) {
            RemoveDataMessage removeDataMessage = (RemoveDataMessage) msg;
            node.removeData(removeDataMessage.getKey());
            commits.remove(removeDataMessage.getKey());
        }

    }

    private boolean checkInput(String[] s) {
        boolean flag = false;
        if ("get".equals(s[0]) && s.length == 2) {
            flag = true;
        }
        if ("remove".equals(s[0]) && s.length == 2) {
            flag = true;
        }
        if ("set".equals(s[0]) && s.length == 3) {
            flag = true;
        }
        if ("quit".equals(s[0]) && s.length == 1) {
            flag = true;
        }
        return flag;
    }
}
